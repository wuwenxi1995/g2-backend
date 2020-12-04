package org.g2.scheduler.api.controller;

import org.g2.core.util.Results;
import org.g2.scheduler.app.service.JobInfoService;
import org.g2.scheduler.domain.entity.JobInfo;
import org.g2.scheduler.domain.repositoty.JobInfoRepository;
import org.g2.scheduler.domain.service.IJobService;
import org.g2.starter.mybatis.page.PageRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author wenxi.wu@hand-chian.com 2020-12-01
 */
@RestController("jobInfoController.v1")
@RequestMapping("/job-info")
public class JobInfoController {

    @Autowired
    private JobInfoService jobInfoService;

    @Autowired
    private JobInfoRepository jobInfoRepository;

    @Autowired
    private IJobService jobService;

    @GetMapping
    public ResponseEntity<?> pageJobInfo(JobInfo jobInfo, PageRequest pageRequest) {
        return Results.success();
    }

    @PostMapping("/create")
    public ResponseEntity<?> createJob(@RequestBody JobInfo jobInfo) {
        return Results.success(jobInfoService.createJob(jobInfo));
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateJob(@RequestBody JobInfo jobInfo) {
        return Results.success(jobInfoService.updateJob(jobInfo));
    }

    public ResponseEntity<?> deleteJob(@RequestBody List<JobInfo> jobInfoList) {
        jobInfoService.removeJob(jobInfoList);
        return Results.success();
    }

    @PostMapping("/trigger")
    public ResponseEntity<?> trigger(@RequestParam Long jobId) {
        JobInfo jobInfo = jobInfoRepository.selectByPrimaryKey(jobId);
        if (null != jobInfo) {
            jobService.trigger(jobId);
        }
        return Results.success();
    }

    @PostMapping("/pause")
    public ResponseEntity<?> pause(@RequestParam Long jobId) {
        JobInfo jobInfo = jobInfoRepository.selectByPrimaryKey(jobId);
        if (null != jobInfo) {
            jobService.pauseJob(jobId);
        }
        return Results.success();
    }

    @PostMapping("/resume")
    public ResponseEntity<?> resume(@RequestParam Long jobId) {
        JobInfo jobInfo = jobInfoRepository.selectByPrimaryKey(jobId);
        if (null != jobInfo) {
            jobService.resumeJob(jobId);
        }
        return Results.success();
    }
}
