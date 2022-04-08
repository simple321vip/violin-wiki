package com.g.estate.controller;

import com.g.estate.service.StudyService;
import com.g.estate.vo.SectionVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@CrossOrigin
public class StudyCtl {

    @Autowired
    private StudyService studyService;

    @RequestMapping("/study/sections")
    public ResponseEntity<List<SectionVo>> getSections(){
        return new ResponseEntity<>(studyService.getStudy(1), HttpStatus.OK);
    }
}
