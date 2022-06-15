package com.dv.cashlog.api.controller;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dv.cashlog.api.request.MajorRequestModel;
import com.dv.cashlog.api.response.MajorResponseModel;
import com.dv.cashlog.common.dto.MajorDto;
import com.dv.cashlog.service.MajorService;

@RestController
@RequestMapping("/v1/major")
public class MajorController {

    @Autowired
    private MajorService majorService;

    @PostMapping("/create")
    public MajorResponseModel createMajor(@RequestBody MajorRequestModel majorReq) {
        ModelMapper modelMapper = new ModelMapper();
        MajorDto majorDtoReq = modelMapper.map(majorReq, MajorDto.class);
        MajorDto majorDtoResp = majorService.createMajor(majorDtoReq);

        MajorResponseModel majorResp = modelMapper.map(majorDtoResp, MajorResponseModel.class);
        return majorResp;
    }

    @GetMapping("/get/{id}")
    public MajorResponseModel getMajor(@PathVariable long id) {
        ModelMapper modelMapper = new ModelMapper();

        MajorDto majorDtoResp = majorService.getMajor(id);

        MajorResponseModel majorResp = modelMapper.map(majorDtoResp, MajorResponseModel.class);
        return majorResp;
    }

    @GetMapping("/get/all")
    public List<MajorResponseModel> getMajors(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "limit", defaultValue = "5") int limit) {
        List<MajorResponseModel> majorsResp = new ArrayList<>();
        List<MajorDto> majorsDtoResp = majorService.getMajors(page, limit);

        ModelMapper modelMapper = new ModelMapper();
        for (MajorDto majorDto : majorsDtoResp) {
            MajorResponseModel majorResp = modelMapper.map(majorDto, MajorResponseModel.class);
            majorsResp.add(majorResp);
        }

        return majorsResp;
    }
}
