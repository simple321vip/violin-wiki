package cn.violin.wiki.controller;

import cn.violin.common.annotation.CurrentUser;
import cn.violin.common.entity.Tenant;
import cn.violin.wiki.form.SortData;
import cn.violin.wiki.form.WikiForm;
import cn.violin.wiki.service.WikiService;
import cn.violin.wiki.vo.WikiVo;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

@Controller
@RequestMapping("/api/v1/author")
@AllArgsConstructor
@CrossOrigin
public class WikiApi {

    @Autowired
    private WikiService wikiService;

    @PostMapping("/wiki")
    @ResponseBody
    public ResponseEntity<Void> create(@Valid @RequestBody WikiForm wikiForm, @CurrentUser Tenant tenant) throws Exception {
        wikiService.create(wikiForm, tenant);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/wiki/{id}")
    @ResponseBody
    public ResponseEntity<Void> delete(@NotBlank @PathVariable("id") String id, @CurrentUser Tenant tenant) throws Exception {
        wikiService.delete(id, tenant);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/wiki/{id}")
    @ResponseBody
    public ResponseEntity<Void> update(@NotBlank @PathVariable("id") String id, @Valid @RequestBody WikiForm wikiForm,
                                       @CurrentUser Tenant tenant) throws Exception {
        wikiService.update(id, wikiForm, tenant);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/wiki/{id}")
    @ResponseBody
    public ResponseEntity<WikiVo> get(@PathVariable(value = "id") String id, @CurrentUser Tenant tenant) throws Exception {
        WikiVo result = wikiService.get(id, tenant);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping(value = "/wiki/{btId}", produces = {"application/json"})
    @ResponseBody
    public ResponseEntity<Void> sort(@PathVariable(value = "btId") String btId, @Valid @RequestBody SortData sortData, @CurrentUser Tenant tenant) throws Exception {

        wikiService.sortWiki(btId, sortData.getBIds(), tenant);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
