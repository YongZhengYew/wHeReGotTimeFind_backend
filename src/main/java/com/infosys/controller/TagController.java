package com.infosys.controller;

import com.infosys.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tags")
public class TagController {
    @Autowired
    private TagService tagService;

    @GetMapping("/tagNames/{tagName}")
    String getTagsByTagName(@PathVariable String tagName) {
        return tagService.getByNameList(tagName).toString();
    }
}
