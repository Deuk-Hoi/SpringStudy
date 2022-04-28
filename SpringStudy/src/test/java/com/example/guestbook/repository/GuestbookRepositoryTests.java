package com.example.guestbook.repository;


import com.example.guestbook.dto.GuestbookDTO;
import com.example.guestbook.dto.PageRequestDTO;
import com.example.guestbook.dto.PageResultDTO;
import com.example.guestbook.entity.GuestBook;
import com.example.guestbook.entity.QGuestBook;
import com.example.guestbook.service.GuestbookService;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Optional;
import java.util.stream.IntStream;

@SpringBootTest
public class GuestbookRepositoryTests {

    @Autowired
    private GuestbookRepository guestbookRepository;

    @Autowired
    private GuestbookService service;

    @Test
    public void insertDummies(){
        IntStream.rangeClosed(1, 300).forEach(i->{
            GuestBook guestBook = GuestBook.builder()
                    .title("Title...."+i)
                    .content("Content..."+i)
                    .writer("user..."+i)
                    .build();
            System.out.println(guestbookRepository.save(guestBook));
        });
    }

    @Test
    public void updateTest(){
        Optional<GuestBook> result = guestbookRepository.findById(300L);

        if(result.isPresent()){
            GuestBook guestBook = result.get();
            guestBook.changeTitle("Change Title");
            guestBook.changContent("Change Content");

            guestbookRepository.save(guestBook);
        }
    }

    @Test
    public void testQuery1(){
        Pageable pageable = PageRequest.of(0, 10, Sort.by("gno").descending());
        String keyword = "1";
        QGuestBook qGuestBook = QGuestBook.guestBook;
        BooleanBuilder booleanBuilder = new BooleanBuilder();

        //title like %1%
        BooleanExpression expression = qGuestBook.title.contains(keyword);
        booleanBuilder.and(expression);

        Page<GuestBook> result = guestbookRepository.findAll(booleanBuilder, pageable);

        result.stream().forEach(guestBook -> {
            System.out.println(guestBook);
        });
    }

    @Test
    public void testQuery2(){
        Pageable pageable = PageRequest.of(0, 10, Sort.by("gno").descending());
        String keyword = "1";
        QGuestBook qGuestBook = QGuestBook.guestBook;
        BooleanBuilder booleanBuilder = new BooleanBuilder();

        //title like %1% or content like %1%;
        BooleanExpression expression = qGuestBook.title.contains(keyword);
        expression = expression.or(qGuestBook.content.contains(keyword));

        booleanBuilder.and(expression);
        booleanBuilder.and(qGuestBook.gno.gt(0L));

        Page<GuestBook> result = guestbookRepository.findAll(booleanBuilder, pageable);

        result.stream().forEach(guestBook -> {
            System.out.println(guestBook);
        });
    }

    @Test
    public void testList(){
        PageRequestDTO pageRequestDTO = PageRequestDTO.builder().page(1).size(10).build();
        PageResultDTO<GuestbookDTO, GuestBook> resultDTO = service.getList(pageRequestDTO);

        System.out.println("PREV: "+resultDTO.isPrev());
        System.out.println("NEXT: "+resultDTO.isNext());
        System.out.println("TOTAL: "+resultDTO.getTotalPage());
        System.out.println("=====================================");
        for(GuestbookDTO guestbookDTO : resultDTO.getDtoList()){
            System.out.println(guestbookDTO);
        }
        System.out.println("=====================================");
        resultDTO.getPageList().forEach(i -> System.out.println(i));
    }


}
