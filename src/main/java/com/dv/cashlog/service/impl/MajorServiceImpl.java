package com.dv.cashlog.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.dv.cashlog.common.dto.MajorDto;
import com.dv.cashlog.io.entity.MajorEntity;
import com.dv.cashlog.io.repository.MajorRepository;
import com.dv.cashlog.service.MajorService;

@Service
public class MajorServiceImpl implements MajorService {

    @Autowired
    private MajorRepository majorRepository;

    @Override
    public MajorDto createMajor(MajorDto majorDtoReq) {

        MajorEntity majorExistenceChecking = majorRepository.findByName(majorDtoReq.getName());
        if (majorExistenceChecking != null) {
            throw new RuntimeException("Major was existed!!!");
        }

        ModelMapper modelMapper = new ModelMapper();
        MajorEntity majorEntityReq = modelMapper.map(majorDtoReq, MajorEntity.class);
        MajorEntity majorEntityResp = majorRepository.save(majorEntityReq);

        MajorDto majorDtoResp = modelMapper.map(majorEntityResp, MajorDto.class);
        return majorDtoResp;
    }

    @Override
    public MajorDto getMajor(long id) {
        Optional<MajorEntity> majorEntity = majorRepository.findById(id);
        if (!majorEntity.isPresent()) {
            throw new RuntimeException("No major found!!!");
        }
        ModelMapper modelMapper = new ModelMapper();
        MajorDto majorDtoResp = modelMapper.map(majorEntity.get(), MajorDto.class);
        return majorDtoResp;
    }

    @Override
    public List<MajorDto> getMajors(int page, int limit) {
        List<MajorDto> majorsDtoResp = new ArrayList<>();

        page = page > 0 ? page - 1 : 0;

        Pageable pageableReq = PageRequest.of(page, limit);

        Page<MajorEntity> majorPage = majorRepository.findAll(pageableReq);
        List<MajorEntity> majors = majorPage.getContent();

        ModelMapper modelMapper = new ModelMapper();
        for (MajorEntity major : majors) {
            MajorDto majorDto = modelMapper.map(major, MajorDto.class);
            majorsDtoResp.add(majorDto);
        }

        return majorsDtoResp;
    }

}
