package com.company.courtmanagement.view.util.mapper;

import com.company.courtmanagement.model.CourtHearing;
import com.company.courtmanagement.model.CourtStage;
import com.company.courtmanagement.view.dto.input.CourtHearingInputDto;
import com.company.courtmanagement.view.dto.output.CourtHearingOutputDto;
import org.modelmapper.AbstractConverter;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class CourtHearingMapper {
    @Autowired
    private ApplicationContext context;

    public CourtHearing mapToModel(CourtHearingInputDto dto) {
        Converter<Long, CourtHearing> hearingConverter = new AbstractConverter<>() {
            @Override
            protected CourtHearing convert(Long source) {
                return source == null ? null : new CourtHearing(source);
            }
        };
        Converter<Long, CourtStage> stageConverter = new AbstractConverter<>() {
            @Override
            protected CourtStage convert(Long source) {
                return source == null ? null : new CourtStage(source);
            }
        };
        ModelMapper mapper = context.getBean(ModelMapper.class);
        mapper.addConverter(hearingConverter);
        mapper.addConverter(stageConverter);
        return mapper.map(dto, CourtHearing.class);
    }

    public CourtHearingOutputDto mapToDto(CourtHearing courtHearing) {
        Converter<CourtStage, Long> stageConverter = new AbstractConverter<>() {
            @Override
            protected Long convert(CourtStage source) {
                return source == null ? null : source.getCourtStageId();
            }
        };
        ModelMapper mapper = context.getBean(ModelMapper.class);
        mapper.addConverter(stageConverter);
        return mapper.map(courtHearing, CourtHearingOutputDto.class);
    }
}
