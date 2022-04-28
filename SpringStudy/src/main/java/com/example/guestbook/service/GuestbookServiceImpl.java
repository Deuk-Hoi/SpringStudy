package com.example.guestbook.service;

import com.example.guestbook.dto.GuestbookDTO;
import com.example.guestbook.dto.PageRequestDTO;
import com.example.guestbook.dto.PageResultDTO;
import com.example.guestbook.entity.GuestBook;
import com.example.guestbook.entity.QGuestBook;
import com.example.guestbook.repository.GuestbookRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.function.Function;

@Service
@Log4j2
@RequiredArgsConstructor
public class GuestbookServiceImpl implements GuestbookService{

    private final GuestbookRepository repository;

    @Override
    public Long register(GuestbookDTO guestbookDTO) {
        log.info("DTO------------------------");
        log.info(guestbookDTO);
        GuestBook entity = dtoToEntity(guestbookDTO);
        log.info(entity);
        repository.save(entity);
        return entity.getGno();
    }

    @Override
    public PageResultDTO<GuestbookDTO, GuestBook> getList(PageRequestDTO requestDTO) {
        Pageable pageable = requestDTO.getPageable(Sort.by("gno").descending());
        BooleanBuilder booleanBuilder = getSearch(requestDTO);
        Page<GuestBook> result = repository.findAll(booleanBuilder, pageable);
        Function<GuestBook, GuestbookDTO> fn = (entity ->entityToDto(entity));

        return new PageResultDTO<>(result, fn);
    }

    @Override
    public GuestbookDTO read(Long gno) {
        Optional<GuestBook> result = repository.findById(gno);
        return result.isPresent() ? entityToDto(result.get()):null;
    }

    @Override
    public void remove(Long gno) {
        repository.deleteById(gno);
    }

    @Override
    public void modify(GuestbookDTO dto) {
        Optional<GuestBook> result = repository.findById(dto.getGno());
        if(result.isPresent()){
            GuestBook entity = result.get();
            entity.changeTitle(dto.getTitle());
            entity.changContent(dto.getContent());
            repository.save(entity);
        }
    }

    private BooleanBuilder getSearch(PageRequestDTO requestDTO){
        String type = requestDTO.getType();
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        QGuestBook qGuestBook = QGuestBook.guestBook;

        String keyword = requestDTO.getKeyword();
        BooleanExpression expression = qGuestBook.gno.gt(0L);
        booleanBuilder.and(expression);

        if(type == null || type.trim().length() == 0){
            return booleanBuilder;
        }

        BooleanBuilder conditionBuilder = new BooleanBuilder();
        if(type.contains("t")){
            conditionBuilder.or(qGuestBook.title.contains(keyword));
        }
        if(type.contains("c")){
            conditionBuilder.or(qGuestBook.content.contains(keyword));
        }
        if(type.contains("w")){
            conditionBuilder.or(qGuestBook.writer.contains(keyword));
        }

        booleanBuilder.and(conditionBuilder);
        return booleanBuilder;
    }
}
