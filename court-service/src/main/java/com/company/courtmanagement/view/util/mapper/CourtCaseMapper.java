package com.company.courtmanagement.view.util.mapper;

import com.company.courtmanagement.model.Court;
import com.company.courtmanagement.model.CourtCase;
import com.company.courtmanagement.model.CourtStage;
import com.company.courtmanagement.view.dto.input.CourtCaseInputDto;
import com.company.courtmanagement.view.dto.output.CourtCaseOutputDto;
import org.modelmapper.AbstractConverter;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class CourtCaseMapper {
    @Autowired
    private ApplicationContext context;

    public CourtCase mapToModel(CourtCaseInputDto dto) {
        Converter<Long, Court> courtConverter = new AbstractConverter<>() {
            @Override
            protected Court convert(Long source) {
                return source == null ? null : new Court(source);
            }
        };
        Converter<Long, CourtCase> caseConverter = new AbstractConverter<>() {
            @Override
            protected CourtCase convert(Long source) {
                return source == null ? null : new CourtCase(source);
            }
        };
        Converter<Long, CourtStage> stageConverter = new AbstractConverter<>() {
            @Override
            protected CourtStage convert(Long source) {
                return source == null ? null : new CourtStage(source);
            }
        };
        ModelMapper mapper = context.getBean(ModelMapper.class);
        mapper.addConverter(courtConverter);
        mapper.addConverter(caseConverter);
        mapper.addConverter(stageConverter);
        return mapper.map(dto, CourtCase.class);
    }

    public CourtCaseOutputDto mapToDto(CourtCase courtCase) {
        ModelMapper modelMapper = context.getBean(ModelMapper.class);
        return modelMapper.map(courtCase, CourtCaseOutputDto.class);
    }
}
