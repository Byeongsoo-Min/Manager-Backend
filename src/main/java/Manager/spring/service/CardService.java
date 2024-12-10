package Manager.spring.service;


import Manager.spring.domain.Card;
import Manager.spring.repository.CardRepository;
import Manager.spring.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Transactional
@Service
public class CardService {
    private final CardRepository cardRepository;
    private final MemberRepository memberRepository;

    @Autowired
    public CardService(CardRepository cardRepository, MemberRepository memberRepository) {
        this.cardRepository = cardRepository;
        this.memberRepository = memberRepository;
    }

    public Card createCard(Card card, MultipartFile image) {
        uploadCard(card, image);
        return cardRepository.save(card);
    }

    public Optional<Card> getCardById(Long id) {
        return cardRepository.findById(id);
    }

    public List<Card> getCardByName(String name) {
        return cardRepository.findByName(name);
    }

    public List<Card> getCardByMemberId(Long id) {
        return cardRepository.findByMemberId(id);
    }

    public void uploadCard(Card card, MultipartFile image) {
        try {
            String uploadDir = "src/main/resources/static/uploads/card";
            String dbFilePath = saveImage(image, uploadDir);

            card.setCompanyImageUrl(dbFilePath);
            cardRepository.save(card);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String saveImage(MultipartFile image, String uploadsDir) throws IOException {
        String projectBasePath = System.getProperty("user.home") + "/Manager/Manager-Backend";

        // 업로드 디렉토리 경로 설정
        String uploadDir = projectBasePath + "/src/main/resources/static/uploads/";

        // 디렉토리 생성
        File directory = new File(uploadDir);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        // 고유한 파일명 생성
        String fileName = UUID.randomUUID().toString().replace("-", "") + "_" + image.getOriginalFilename();
        String filePath = uploadDir + fileName;

        // DB에 저장될 파일 경로
        String dbFilePath = "/api/v1/card/uploads/" + fileName;

        // 파일 저장
        Path path = Paths.get(filePath);
        Files.write(path, image.getBytes());

        return dbFilePath;
    }

}
