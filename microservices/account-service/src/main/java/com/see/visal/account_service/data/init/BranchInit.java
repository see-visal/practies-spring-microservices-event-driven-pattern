package com.see.visal.account_service.data.init;

import com.see.visal.account_service.data.entity.BranchEntity;
import com.see.visal.account_service.data.repository.BranchRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class BranchInit implements ApplicationListener<ApplicationReadyEvent> {

    private final BranchRepository branchRepository;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        if (branchRepository.count() == 0) {
            log.info("Initializing Branches...");

            BranchEntity mainBranch = new BranchEntity();
            mainBranch.setBranchId(UUID.fromString("770e8400-e29b-41d4-a716-446655440001"));
            mainBranch.setBranchName("Main Branch");
            mainBranch.setIsOpening(true);

            BranchEntity downtownBranch = new BranchEntity();
            downtownBranch.setBranchId(UUID.fromString("770e8400-e29b-41d4-a716-446655440002"));
            downtownBranch.setBranchName("Downtown Branch");
            downtownBranch.setIsOpening(true);

            BranchEntity uptownBranch = new BranchEntity();
            uptownBranch.setBranchId(UUID.fromString("770e8400-e29b-41d4-a716-446655440003"));
            uptownBranch.setBranchName("Uptown Branch");
            uptownBranch.setIsOpening(true);

            branchRepository.saveAll(List.of(mainBranch, downtownBranch, uptownBranch));
            log.info("Initialized 3 branches");
        }
    }
}

