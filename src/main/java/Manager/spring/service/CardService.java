package Manager.spring.service;


import Manager.spring.domain.Card;
import Manager.spring.repository.CardRepository;
import Manager.spring.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

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
        String fileName = UUID.randomUUID().toString().replace("-", "") + "_" + image.getOriginalFilename();
        String filePath = uploadsDir + fileName;
        String dbFilePath = "/api/v1/card/uploads/" + fileName;

        Path path = Paths.get(filePath);
        Files.createDirectories(path.getParent());
        Files.write(path, image.getBytes());

        return dbFilePath;
    }

}
