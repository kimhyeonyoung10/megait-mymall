package com.megait.mymall.service;

import com.megait.mymall.domain.Album;
import com.megait.mymall.domain.Book;
import com.megait.mymall.domain.Item;
import com.megait.mymall.domain.Member;
import com.megait.mymall.repository.AlbumRepository;
import com.megait.mymall.repository.BookRepository;
import com.megait.mymall.repository.ItemRepository;
import com.megait.mymall.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class ItemService {
    private final BookRepository bookRepository;
    private final AlbumRepository albumRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;
    // ItemService가 빈으로 등록되기 위해 객체 생성(new)이 될것임.
    // 그 객체 생성이 된 이후에 무엇을 할 지 : @PostConstruct
    @PostConstruct
    public void saveBookItems() throws IOException {
        // book.csv의 내용을 토대로 상품 DB에 저장

        // classPath: main - java - resources(일반 파일)를 의미함!
        // classPath/csv/book.csv를 가져옴.
        ClassPathResource resource = new ClassPathResource("csv/book.csv"); // ctrl + alt + v return값 자동 생성 짱 중요!! 커서를 맨 끝에 두기

        // 가져온 csv 파일의 모든 라인을 읽어들이기
        List<String> stringList = Files.readAllLines(resource.getFile().toPath(), StandardCharsets.UTF_8);

        /* List<Book> bookList = new ArrayList<>();
        for(String s : stringList){
            String[] arr = s.split("\\|"); // \\| ~> (정규식에서) 예약어가 아닌 문자 그대로의 '|' 라는 의미
            Book book = Book.builder()
                    .name(arr[0])
                    .imageUrl(arr[1])
                    .price(Integer.parseInt(arr[2]))
                    .build();
            bookList.add(book);
        } */

//        Stream<String> stream = stringList.stream();
//        Stream<Book> objectStream = stream.map(line -> {
//            String[] arr = line.split("\\|");
//            return Book.builder()
//                    .name(arr[0]).imageUrl(arr[1])
//                    .price(Integer.parseInt(arr[2]))
//                    .build();
//        });
//        List<Book> bookList = objectStream.collect();
//        bookRepository.saveAll(bookList);

        List<Book> bookList = Files.readAllLines(resource.getFile().toPath(), StandardCharsets.UTF_8)
                .stream()
                .map(line -> {
                    String[] arr = line.split("\\|");
                    return Book.builder()
                            .name(arr[0])
                            .imageUrl(arr[1])
                            .price(Integer.parseInt(arr[2]))
                            .build();
                })
                .collect(Collectors.toList());

        bookRepository.saveAll(bookList);

        log.info("북리스트 : "+ stringList);
    }



    @PostConstruct
    public void saveAlbumItems() throws IOException {
        // album.csv의 내용을 토대로 상품 DB에 저장
        ClassPathResource resource = new ClassPathResource("csv/album.csv");
        List<String> stringList = Files.readAllLines(resource.getFile().toPath(), StandardCharsets.UTF_8);
        List<Album> albumList = Files.readAllLines(resource.getFile().toPath(), StandardCharsets.UTF_8)
                .stream()
                .map(line -> {
                    String[] arr = line.split("\\|");
                    return Album.builder()
                            .name(arr[0])
                            .imageUrl(arr[1])
                            .price(Integer.parseInt(arr[2]))
                            .build();
                })
                .collect(Collectors.toList());

        albumRepository.saveAll(albumList);
    }

    public List<Book> getBookList() {
        return bookRepository.findAll();
    }

    public List<Album> getAlbumList(){
        return albumRepository.findAll();
    }

    public Item getItem(Long id) {

        // Album db에 있는지 확인!
        Optional<Album> optional = albumRepository.findById(id);
        if(optional.isPresent()){
            return optional.get();
        }

        Optional<Book> optional2 = bookRepository.findById(id);
        return optional2.orElse(null);
    }

    @Transactional
    public void addLike(Member member, Long id) {

        Item item;
        Optional<Album> album = albumRepository.findById(id);
        if (album.isPresent()) {
            item = album.get();
        } else {
            Optional<Book> book = bookRepository.findById(id);
            item = book.orElseThrow(() -> new IllegalArgumentException("잘못된 상품 번호입니다."));
        }


        member = memberRepository.findByEmail(member.getEmail()).orElse(null);
        if (member == null) {
            throw new IllegalArgumentException("잘못된 회원입니다.");
        }
        List<Item> list = member.getLikes();

        if (list.contains(item)) {
            throw new IllegalArgumentException("이미 찜한 상품입니다.");
        }
        list.add(item);
        item.setLiked(item.getLiked() + 1);

    }

    @Transactional
    public void deleteLikes(Member member, List<Long> idList) {
        member = memberRepository.getById(member.getId());
        member.getLikes().removeAll(itemRepository.findAllById(idList));
    }
}
