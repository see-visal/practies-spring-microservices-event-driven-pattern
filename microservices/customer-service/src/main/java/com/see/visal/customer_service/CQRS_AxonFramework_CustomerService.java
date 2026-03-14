/**
 * ============================================================
 *   CQRS with Axon Framework — Customer Service Overview
 * ============================================================
 *
 * CQRS (Command Query Responsibility Segregation) separates the
 * write model (Commands) from the read model (Queries).
 * Axon Framework provides the infrastructure (Command Bus, Event Bus,
 * Query Bus, Event Store) to implement CQRS + Event Sourcing easily.
 *
 * ─────────────────────────────────────────────────────────────
 *  ARCHITECTURE OVERVIEW
 * ─────────────────────────────────────────────────────────────
 *
 *   REST Controller
 *       │
 *       ▼
 *   Service Layer (CustomerServiceImpl)
 *       │  sends Command via CommandGateway
 *       ▼
 *  ┌─────────────────────────────────────────────────────────┐
 *  │                  COMMAND SIDE (Write)                   │
 *  │                                                         │
 *  │  Command ──► Aggregate (@CommandHandler)                │
 *  │                  │ AggregateLifecycle.apply(event)      │
 *  │                  ▼                                      │
 *  │             Event Store (Axon)                          │
 *  │                  │                                      │
 *  │                  ▼                                      │
 *  │  Aggregate (@EventSourcingHandler) ◄── state rebuild    │
 *  └─────────────────────────────────────────────────────────┘
 *       │  event published to Event Bus
 *       ▼
 *  ┌─────────────────────────────────────────────────────────┐
 *  │                  READ SIDE (Projection)                 │
 *  │                                                         │
 *  │  CustomerListener (@EventHandler)                       │
 *  │      │  saves/updates read-model in PostgreSQL          │
 *  │      ▼                                                  │
 *  │  CustomerEntity (JPA / PostgreSQL)                      │
 *  └─────────────────────────────────────────────────────────┘
 *       ▲
 *       │  query fetched via QueryGateway
 *  ┌─────────────────────────────────────────────────────────┐
 *  │                  QUERY SIDE (Read)                      │
 *  │                                                         │
 *  │  Query ──► CustomerQueryHandler (@QueryHandler)         │
 *  │                │  reads from CustomerRepository (JPA)   │
 *  │                ▼                                        │
 *  │          CustomPageResponse (DTO)                       │
 *  └─────────────────────────────────────────────────────────┘
 *
 * ─────────────────────────────────────────────────────────────
 *  PACKAGE STRUCTURE
 * ─────────────────────────────────────────────────────────────
 *
 *  com.see.visal.customer_service
 *  ├── rest/
 *  │   ├── CustomerController.java          → POST /api/customers
 *  │   │                                      PUT  /api/customers/{id}/phone-number
 *  │   └── CustomerQueryController.java     → GET  /api/customers (paged)
 *  │
 *  ├── application/
 *  │   ├── CustomerService.java             → Command-side service interface
 *  │   ├── CustomerServiceImpl.java         → Sends commands via CommandGateway
 *  │   ├── CustomerQueryService.java        → Query-side service interface
 *  │   ├── CustomerQueryServiceImpl.java    → Sends queries via QueryGateway
 *  │   ├── dto/
 *  │   │   ├── create/  CreateCustomerRequest, CreateCustomerResponse
 *  │   │   ├── update/  ChangePhoneNumberRequest, ChangePhoneNumberResponse
 *  │   │   └── query/   GetCustomerQuery, CustomerResponse,
 *  │   │                CustomerSegmentResponse, CustomPageResponse
 *  │   ├── mapper/
 *  │   │   └── CustomerApplicationMapper.java  (MapStruct)
 *  │   ├── listener/
 *  │   │   └── CustomerListener.java        → @EventHandler (projection updater)
 *  │   └── projecttion/
 *  │       ├── GetCustomerQuery.java        → Query object (record)
 *  │       └── CustomerQueryHandler.java    → @QueryHandler reads DB & returns DTO
 *  │
 *  ├── domain/
 *  │   ├── aggregate/
 *  │   │   ├── CustomerAggregate.java       → @Aggregate, @CommandHandler, @EventSourcingHandler
 *  │   │   └── CustomerSegmentAggregate.java
 *  │   ├── commend/
 *  │   │   ├── CreateCustomerCommand.java   → record, @TargetAggregateIdentifier
 *  │   │   └── ChangePhoneNumberCommand.java
 *  │   ├── event/
 *  │   │   ├── CustomerCreatedEvent.java    → record (immutable event)
 *  │   │   └── CustomerPhoneNumberChangedEvent.java
 *  │   └── valueobject/
 *  │       ├── CustomerName, CustomerEmail, CustomerGender
 *  │       ├── Address, Contact, Kyc
 *  │       └── CustomerSegmentType
 *  │
 *  └── data/
 *      ├── entity/
 *      │   ├── CustomerEntity.java          → JPA entity (read-model table)
 *      │   ├── AddressEntity, ContactEntity, KycEntity, CustomerSegmentEntity
 *      └── repository/
 *          ├── CustomerRepository.java
 *          └── CustomerSegmentRepository.java
 *
 * ─────────────────────────────────────────────────────────────
 *  COMMAND FLOW — Create Customer
 * ─────────────────────────────────────────────────────────────
 *
 *  Step 1 ─ REST Layer
 *    POST /api/customers
 *    Body: CreateCustomerRequest { customerName, customerEmail, dob,
 *                                  customerGender, kyc, address,
 *                                  contact, customerSegmentId, phoneNumber }
 *
 *  Step 2 ─ Service Layer (CustomerServiceImpl)
 *    CreateCustomerCommand createCustomerCommand =
 *        customerMapper.createCustomerRequestToCreateCustomerCommand(
 *            new CustomerId(UUID.randomUUID()), createCustomerRequest);
 *    CustomerId result = commandGateway.sendAndWait(createCustomerCommand);
 *
 *  Step 3 ─ Command (record)
 *    public record CreateCustomerCommand(
 *        @TargetAggregateIdentifier CustomerId customerId,   // Axon routes by this
 *        CustomerName customerName,
 *        CustomerEmail customerEmail,
 *        CustomerGender customerGender,
 *        String phoneNumber,
 *        LocalDate dob,
 *        Kyc kyc,
 *        Address address,
 *        Contact contact,
 *        CustomerSegmentId customerSegmentId
 *    ) {}
 *
 *  Step 4 ─ Aggregate (@CommandHandler constructor)
 *    @Aggregate(snapshotTriggerDefinition = "customerSnapshotTriggerDefinition")
 *    public class CustomerAggregate {
 *
 *        @AggregateIdentifier
 *        private CustomerId customerId;
 *
 *        @CommandHandler  // called by Axon for new aggregate instances
 *        public CustomerAggregate(CreateCustomerCommand cmd) {
 *            validateEmail(cmd.customerEmail());
 *            validatePhoneNumber(cmd.phoneNumber());
 *
 *            CustomerCreatedEvent event = CustomerCreatedEvent.builder()
 *                .customerId(cmd.customerId()) ...build();
 *
 *            AggregateLifecycle.apply(event);  // persist to event store + publish
 *        }
 *    }
 *
 *  Step 5 ─ Event (record)
 *    @Builder
 *    public record CustomerCreatedEvent(
 *        CustomerId customerId, CustomerName customerName,
 *        CustomerEmail customerEmail, String phoneNumber,
 *        CustomerGender customerGender, LocalDate dob,
 *        Kyc kyc, Address address, Contact contact,
 *        CustomerSegmentId customerSegmentId
 *    ) {}
 *
 *  Step 6 ─ EventSourcingHandler (state reconstruction)
 *    @EventSourcingHandler
 *    public void on(CustomerCreatedEvent e) {
 *        this.customerId       = e.customerId();
 *        this.customerName     = e.customerName();
 *        this.customerEmail    = e.customerEmail();
 *        this.phoneNumber      = e.phoneNumber();
 *        this.customerGender   = e.customerGender();
 *        this.dob              = e.dob();
 *        this.kyc              = e.kyc();
 *        this.address          = e.address();
 *        this.contact          = e.contact();
 *        this.customerSegmentId = e.customerSegmentId();
 *    }
 *    // Axon replays all stored events to rebuild aggregate state on each load
 *
 *  Step 7 ─ EventHandler / Projection (CustomerListener)
 *    @ProcessingGroup("customer-group")
 *    @EventHandler
 *    @Transactional(propagation = Propagation.REQUIRES_NEW)
 *    public void on(CustomerCreatedEvent e) {
 *        // Idempotency guard
 *        if (customerRepository.existsById(e.customerId().getValue())) return;
 *
 *        CustomerEntity entity = mapper.customerCreateEventToCustomerEntity(e);
 *        entity.getAddress().setCustomer(entity);   // back-reference wiring
 *        entity.getContact().setCustomer(entity);
 *        entity.getKyc().setCustomer(entity);
 *
 *        CustomerSegmentEntity segment =
 *            customerSegmentRepository.findById(e.customerSegmentId().customerSegmentId())
 *                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Segment not found"));
 *        entity.setCustomerSegment(segment);
 *
 *        customerRepository.save(entity);           // write to PostgreSQL read-model
 *    }
 *
 * ─────────────────────────────────────────────────────────────
 *  COMMAND FLOW — Change Phone Number
 * ─────────────────────────────────────────────────────────────
 *
 *  PUT /api/customers/{customerId}/phone-number
 *  Body: ChangePhoneNumberRequest { phoneNumber }
 *
 *  Service:
 *    ChangePhoneNumberCommand cmd = ChangePhoneNumberCommand.builder()
 *        .customerId(new CustomerId(customerId))
 *        .phoneNumber(request.phoneNumber())
 *        .build();
 *    CustomerId result = commandGateway.sendAndWait(cmd);
 *
 *  Aggregate (@CommandHandler method — NOT constructor):
 *    @CommandHandler
 *    public CustomerId handle(ChangePhoneNumberCommand cmd) {
 *        validatePhoneNumber(cmd.phoneNumber());
 *        AggregateLifecycle.apply(
 *            CustomerPhoneNumberChangedEvent.builder()
 *                .customerId(cmd.customerId())
 *                .phoneNumber(cmd.phoneNumber())
 *                .build()
 *        );
 *        return cmd.customerId();
 *    }
 *
 *    @EventSourcingHandler
 *    public void on(CustomerPhoneNumberChangedEvent e) {
 *        this.customerId   = e.customerId();
 *        this.phoneNumber  = e.phoneNumber();
 *    }
 *
 *  EventHandler (CustomerListener):
 *    @EventHandler
 *    public void on(CustomerPhoneNumberChangedEvent e) {
 *        CustomerEntity entity = customerRepository
 *            .findById(e.customerId().getValue()).orElseThrow(...);
 *        entity.setPhoneNumber(e.phoneNumber());
 *        // also updates ContactEntity.phoneNumber if present
 *        customerRepository.save(entity);
 *    }
 *
 * ─────────────────────────────────────────────────────────────
 *  QUERY FLOW — Get All Customers (Paged)
 * ─────────────────────────────────────────────────────────────
 *
 *  Step 1 ─ REST Layer
 *    GET /api/customers?pageNumber=0&pageSize=10
 *    → CustomerQueryController → CustomerQueryServiceImpl
 *
 *  Step 2 ─ Query Service
 *    GetCustomerQuery query = new GetCustomerQuery(pageNumber, pageSize);
 *    CustomPageResponse response =
 *        queryGateway.query(query, ResponseTypes.instanceOf(CustomPageResponse.class)).join();
 *
 *  Step 3 ─ Query Object (record)
 *    public record GetCustomerQuery(int pageNumber, int pageSize) {}
 *
 *  Step 4 ─ QueryHandler (CustomerQueryHandler)
 *    @Component
 *    @QueryHandler
 *    public CustomPageResponse handle(GetCustomerQuery query) {
 *        Pageable pageable = PageRequest.of(
 *            query.pageNumber(), query.pageSize(),
 *            Sort.by(Sort.Direction.DESC, "dob")
 *        );
 *        Page<CustomerEntity> page = customerRepository.findAll(pageable);
 *        return customerApplicationMapper.toCustomPageResponse(page);
 *    }
 *
 *  Step 5 ─ Response DTO
 *    @Builder
 *    public record CustomPageResponse(
 *        List<CustomerResponse> content,
 *        int pageNumber,
 *        int pageSize,
 *        long totalElements,
 *        int totalPages
 *    ) {}
 *
 *    public record CustomerResponse(
 *        UUID customerId, CustomerName customerName,
 *        CustomerEmail customerEmail, LocalDate dob,
 *        CustomerGender customerGender, Kyc kyc,
 *        Address address, Contact contact,
 *        String phoneNumber, CustomerSegmentResponse customerSegment
 *    ) {}
 *
 * ─────────────────────────────────────────────────────────────
 *  KEY AXON ANNOTATIONS — QUICK REFERENCE
 * ─────────────────────────────────────────────────────────────
 *
 *  @Aggregate                  Marks the class as an Axon aggregate (event-sourced write model)
 *  @AggregateIdentifier        Field used by Axon to uniquely identify an aggregate instance
 *  @TargetAggregateIdentifier  Field in a Command that identifies which aggregate to route to
 *  @CommandHandler             Method (or constructor) that handles an incoming Command
 *  @EventSourcingHandler       Method that rebuilds aggregate state from a stored Event
 *  AggregateLifecycle.apply()  Persists the event to the event store AND publishes it on the bus
 *  @EventHandler               Handles events on the projection / read-model side
 *  @QueryHandler               Handles queries dispatched via QueryGateway
 *  @ProcessingGroup            Groups multiple @EventHandler beans into one tracking processor
 *
 * ─────────────────────────────────────────────────────────────
 *  SNAPSHOT TRIGGER
 * ─────────────────────────────────────────────────────────────
 *
 *  @Aggregate(snapshotTriggerDefinition = "customerSnapshotTriggerDefinition")
 *
 *  After N events, Axon takes a snapshot of the aggregate state.
 *  On next load it starts from the snapshot + only the newer events,
 *  avoiding expensive full-history replay.
 *
 *  Configured as a Spring Bean (e.g. in config/):
 *    @Bean
 *    public SnapshotTriggerDefinition customerSnapshotTriggerDefinition(
 *            Snapshotter snapshotter) {
 *        return new EventCountSnapshotTriggerDefinition(snapshotter, 50);
 *    }
 *
 * ─────────────────────────────────────────────────────────────
 *  IDEMPOTENCY IN EVENT HANDLERS
 * ─────────────────────────────────────────────────────────────
 *
 *  During event-replay (e.g. rebuilding the read-model), @EventHandlers
 *  are called again. To avoid duplicate DB inserts:
 *
 *    if (customerRepository.existsById(event.customerId().getValue())) {
 *        log.warn("Already processed — skipping.");
 *        return;
 *    }
 *
 * ─────────────────────────────────────────────────────────────
 *  DEAD-LETTER QUEUE (DLQ)
 * ─────────────────────────────────────────────────────────────
 *
 *  Managed via DeadLetterProcessorController.
 *  When an @EventHandler throws an exception, Axon parks the event
 *  in the Dead-Letter Queue so processing of other events continues.
 *  Ops can inspect & retry / discard letters through the controller.
 *
 * ─────────────────────────────────────────────────────────────
 *  GRADLE DEPENDENCY
 * ─────────────────────────────────────────────────────────────
 *
 *  // build.gradle
 *  implementation 'org.axonframework:axon-spring-boot-starter:4.13.0'
 *
 * ─────────────────────────────────────────────────────────────
 *  DATA FLOW SUMMARY DIAGRAM
 * ─────────────────────────────────────────────────────────────
 *
 *  HTTP Request
 *      │
 *      ▼
 *  CustomerController / CustomerQueryController
 *      │                           │
 *      │ (write)                   │ (read)
 *      ▼                           ▼
 *  CustomerServiceImpl     CustomerQueryServiceImpl
 *      │                           │
 *      │ CommandGateway             │ QueryGateway
 *      ▼                           ▼
 *  ╔═══════════════╗         ╔══════════════════════╗
 *  ║ CustomerAggregate ║     ║ CustomerQueryHandler ║
 *  ║ @CommandHandler   ║     ║ @QueryHandler        ║
 *  ║ @EventSourcing    ║     ║  reads PostgreSQL    ║
 *  ╚═══════════════╝         ╚══════════════════════╝
 *          │ apply(event)               ▲
 *          ▼                            │
 *    Event Store (Axon)        CustomerRepository (JPA)
 *          │                            │
 *          │ publish                    │ save
 *          ▼                            │
 *    CustomerListener ───────────────────┘
 *    @EventHandler
 *    (Projection Updater)
 */
package com.see.visal.customer_service;

// This file is intentionally left as documentation only.
// No runtime code is placed here.
public final class CQRS_AxonFramework_CustomerService {
    private CQRS_AxonFramework_CustomerService() {}
}

