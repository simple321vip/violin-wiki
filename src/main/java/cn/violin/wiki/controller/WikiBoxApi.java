package cn.violin.wiki.controller;

import cn.violin.common.annotation.CurrentUser;
import cn.violin.common.entity.Tenant;
import cn.violin.wiki.form.SortData;
import cn.violin.wiki.form.WikiBoxForm;
import cn.violin.wiki.service.WikiBoxService;
import cn.violin.wiki.vo.WikiBoxVo;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Controller
@RequestMapping("/api/v1/author")
@AllArgsConstructor
@CrossOrigin
public class WikiBoxApi {

    @Autowired
    private WikiBoxService wikiBoxService;

    @PostMapping(value = "/wiki/type", produces = {"application/json"})
    @ResponseBody
    public ResponseEntity<Void> create(@Valid @RequestBody() WikiBoxForm wikiBoxForm, @CurrentUser Tenant tenant) {
        wikiBoxService.create(wikiBoxForm, tenant);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping(value = "/wiki/type/{id}", produces = {"application/json"})
    @ResponseBody
    public ResponseEntity<Void> delete(@NotBlank @PathVariable("id") String id, @CurrentUser Tenant tenant) throws Exception {
        wikiBoxService.delete(id, tenant);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping(value = "/wiki/type/{id}", produces = {"application/json"})
    @ResponseBody
    public ResponseEntity<Void> update(@NotBlank @PathVariable("id") String id, @Valid @RequestBody() WikiBoxForm wikiBoxForm, @CurrentUser Tenant tenant) {
        wikiBoxService.update(id, wikiBoxForm, tenant);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(value = "/wiki/type/{btId}", produces = {"application/json"})
    @ResponseBody
    public ResponseEntity<WikiBoxVo> getWikiList(@NotNull @PathVariable(value = "btId") String btId, @CurrentUser Tenant tenant) {
        WikiBoxVo vo = wikiBoxService.get(btId, tenant);
        return new ResponseEntity<>(vo, HttpStatus.OK);
    }

    @GetMapping(value = "/wiki/type", produces = {"application/json"})
    @ResponseBody
    public ResponseEntity<List<WikiBoxVo>> getWikiTypeList(@CurrentUser Tenant tenant) {
        List<WikiBoxVo> vo = wikiBoxService.getWikiTypeList(tenant);
        return new ResponseEntity<>(vo, HttpStatus.OK);
    }

    @PostMapping(value = "/wiki/type/sort", produces = {"application/json"})
    @ResponseBody
    public ResponseEntity<Void> sortWikiType(@Valid @RequestBody() SortData sortData, @CurrentUser Tenant tenant) throws Exception {
        wikiBoxService.sort(sortData.getBtIds(), tenant);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/wiki/list")
    @ResponseBody
    public ResponseEntity<List<WikiBoxVo>> getAll(@CurrentUser Tenant tenant) {
        return new ResponseEntity<>(wikiBoxService.listAll(tenant), HttpStatus.OK);
    }
}
