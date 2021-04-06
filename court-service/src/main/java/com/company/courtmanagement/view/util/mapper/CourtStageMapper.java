package com.company.courtmanagement.view.util.mapper;

import com.company.courtmanagement.model.Court;
import com.company.courtmanagement.model.CourtCase;
import com.company.courtmanagement.model.CourtStage;
import com.company.courtmanagement.view.dto.input.CourtStageInputDto;
import com.company.courtmanagement.view.dto.output.CourtStageOutputDto;
import org.modelmapper.AbstractConverter;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class CourtStageMapper {
    @Autowired
    private ApplicationContext context;

    public CourtStage mapToModel(CourtStageInputDto dto) {
        Converter<Long, CourtCase> caseConverter = new AbstractConverter<>() {
            @Override
            protected CourtCase convert(Long source) {
                return source == null ? null : new CourtCase(source);
            }
        };
        Converter<Long, Court> courtConverter = new AbstractConverter<>() {
            @Override
            protected Court convert(Long source) {
                return source == null ? null : new Court(source);
            }
        };
        ModelMapper mapper = context.getBean(ModelMapper.class);
        mapper.addConverter(caseConverter);
        mapper.addConverter(courtConverter);
        return mapper.map(dto, CourtStage.class);
    }

    public CourtStageOutputDto mapToDto(CourtStage courtStage) {
        Converter<CourtCase, Long> caseConverter = new AbstractConverter<>() {
            @Override
            protected Long convert(CourtCase source) {
                return source == null ? null : source.getCourtCaseId();
            }
        };
        Converter<CourtStage, Long> stageConverter = new AbstractConverter<>() {
            @Override
            protected Long convert(CourtStage source) {
                return source == null ? null : source.getCourtStageId();
            }
        };
        ModelMapper mapper = context.getBean(ModelMapper.class);
        mapper.addConverter(caseConverter);
        mapper.addConverter(stageConverter);
        return mapper.map(courtStage, CourtStageOutputDto.class);
    }
}
