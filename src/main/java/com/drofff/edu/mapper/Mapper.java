package com.drofff.edu.mapper;

import org.modelmapper.ModelMapper;

public class Mapper {

    private ModelMapper modelMapper;

    public Mapper() {
        this.modelMapper = new ModelMapper();
    }

    public ModelMapper getModelMapper() {
        return modelMapper;
    }

}
