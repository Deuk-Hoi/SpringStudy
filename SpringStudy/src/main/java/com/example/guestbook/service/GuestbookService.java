package com.example.guestbook.service;

import com.example.guestbook.dto.GuestbookDTO;
import com.example.guestbook.dto.PageRequestDTO;
import com.example.guestbook.dto.PageResultDTO;
import com.example.guestbook.entity.GuestBook;

public interface GuestbookService {
    Long register(GuestbookDTO guestbookDTO);

    PageResultDTO<GuestbookDTO, GuestBook> getList(PageRequestDTO requestDTO);

    GuestbookDTO read(Long gno);
    void remove(Long gno);
    void modify(GuestbookDTO dto);

    default GuestBook dtoToEntity(GuestbookDTO dto){
        GuestBook entity = GuestBook.builder()
                .gno(dto.getGno())
                .title(dto.getTitle())
                .content(dto.getContent())
                .writer(dto.getWriter())
                .build();
        return entity;
    }

    default GuestbookDTO entityToDto(GuestBook entity){
        System.out.println(entity);
        GuestbookDTO dto = GuestbookDTO.builder()
                .gno(entity.getGno())
                .content(entity.getContent())
                .title(entity.getTitle())
                .writer(entity.getWriter())
                .regDate(entity.getRegDate())
                .modDate(entity.getModDate())
                .build();
        return dto;
    }
}
