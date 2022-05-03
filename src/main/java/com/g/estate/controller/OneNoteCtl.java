package com.g.estate.controller;

import com.g.estate.io.BookmarkIn;
import com.g.estate.service.StudyService;
import com.g.estate.vo.PageVo;
import com.g.estate.vo.SectionVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Controller
@CrossOrigin
@RequestMapping("/api/vi")
public class OneNoteCtl {

    @Autowired
    private StudyService studyService;

    @RequestMapping("/one_note")
    public ResponseEntity<List<SectionVo>> getSections(){
        return new ResponseEntity<>(studyService.getStudy(2), HttpStatus.OK);
    }


    @PostMapping("/one_note/update_page")
    public ResponseEntity<Void> updatePage(@Valid @RequestBody() PageVo input) {
        studyService.updatePage(input);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/one_note/update_section")
    public ResponseEntity<Void> updateSection(@Valid @RequestBody() SectionVo input) {
        studyService.updateSection(input);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/one_note/insert_section")
    public ResponseEntity<SectionVo> putSection() {
        return new ResponseEntity<>(studyService.insertSection(), HttpStatus.OK);
    }

    @PutMapping("/one_note/insert_page")
    public ResponseEntity<PageVo> putPage(@Valid @RequestBody() PageVo input) {
        return new ResponseEntity<>(studyService.insertPage(input), HttpStatus.OK);
    }
}
