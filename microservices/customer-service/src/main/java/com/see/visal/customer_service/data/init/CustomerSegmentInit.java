package com.see.visal.customer_service.data.init;

import com.see.visal.customer_service.data.entity.CustomerSegmentEntity;
import com.see.visal.customer_service.data.repository.CustomerSegmentRepository;
import com.see.visal.customer_service.domain.valueobject.CustomerSegmentType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class CustomerSegmentInit implements ApplicationListener<ApplicationReadyEvent> {

    private final CustomerSegmentRepository customerSegmentRepository;

    @Override
    @Transactional
    public void onApplicationEvent(ApplicationReadyEvent event) {
        if (customerSegmentRepository.count() == 0) {
            log.info("Initializing Customer Segments...");

            CustomerSegmentEntity normal = new CustomerSegmentEntity();
            normal.setCustomerSegmentId(UUID.randomUUID());
            normal.setCustomerSegmentType(CustomerSegmentType.NORMAL);

            CustomerSegmentEntity student = new CustomerSegmentEntity();
            student.setCustomerSegmentId(UUID.randomUUID());
            student.setCustomerSegmentType(CustomerSegmentType.STUDENT);

            CustomerSegmentEntity vip = new CustomerSegmentEntity();
            vip.setCustomerSegmentId(UUID.randomUUID());
            vip.setCustomerSegmentType(CustomerSegmentType.VIP);

            CustomerSegmentEntity business = new CustomerSegmentEntity();
            business.setCustomerSegmentId(UUID.randomUUID());
            business.setCustomerSegmentType(CustomerSegmentType.BUSINESS);

            customerSegmentRepository.saveAll(List.of(normal, student, vip, business));
            log.info("Initialized {} customer segments", 4);
        }
    }
}
