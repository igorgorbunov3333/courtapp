package com.company.courtmanagement.view;

import com.company.courtmanagement.model.CourtStage;
import com.company.courtmanagement.model.Document;
import com.company.courtmanagement.service.DocumentService;
import com.company.courtmanagement.view.dto.input.DocumentInputDto;
import com.company.courtmanagement.view.dto.output.DocumentOutputDto;
import com.company.courtmanagement.view.util.UriPathHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.modelmapper.AbstractConverter;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.Positive;

@RestController
@RequestMapping(UriPathHelper.COURT_STAGES + "/{courtStageId}" + UriPathHelper.DOCUMENTS)
@Api(tags = "Documents", description = "REST API for documents")
public class DocumentController {
    private ModelMapper modelMapper;
    private DocumentService documentService;

    @Autowired
    public DocumentController(ModelMapper modelMapper, DocumentService documentService) {
        this.modelMapper = modelMapper;
        this.documentService = documentService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Document created successfully"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @ApiOperation("Save document")
    public DocumentOutputDto saveDocument(@Valid @RequestBody DocumentInputDto inputDto,
                                          @Valid @Positive @PathVariable long courtStageId) {
        Document document = new Document();
        CourtStage courtStage = new CourtStage(courtStageId);
        document.setCourtStage(courtStage);
        Document savedDocument = documentService.saveDocument(document);

        Converter<CourtStage, Long> stageIdConverter = new AbstractConverter<>() {
            @Override
            protected Long convert(CourtStage source) {
                return source == null ? null : source.getCourtStageId();
            }
        };
        modelMapper.addConverter(stageIdConverter);
        return modelMapper.map(savedDocument, DocumentOutputDto.class);
    }
}
