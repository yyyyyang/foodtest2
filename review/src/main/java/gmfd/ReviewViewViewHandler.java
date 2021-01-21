package gmfd;

import gmfd.config.kafka.KafkaProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class ReviewViewViewHandler {


    @Autowired
    private ReviewViewRepository reviewViewRepository;

    @StreamListener(KafkaProcessor.INPUT)
    public void whenReviewRegistered_then_CREATE_1 (@Payload ReviewRegistered reviewRegistered) {
        try {
            if (reviewRegistered.isMe()) {

                ReviewView reviewView = new ReviewView();
                // view 객체 생성

                // view 객체에 이벤트의 Value 를 set 함
               //reviewView.getId(reviewRegistered.getId());
                reviewView.setUsername(reviewRegistered.getUsername());
                reviewView.setProductname(reviewRegistered.getProductname());
                reviewView.setContent(reviewRegistered.getContent());
                // view 레파지 토리에 save
                reviewViewRepository.save(reviewView);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

/*
    @StreamListener(KafkaProcessor.INPUT)
    public void whenReviewUpdated_then_UPDATE_1(@Payload ReviewUpdated reviewUpdated) {
        try {
            if (reviewUpdated.isMe()) {
                // view 객체 조회
                List<ReviewView> foodCatalogViewList = ReviewViewRepository.(catalogUpdated.getId());
                for(FoodCatalogView foodCatalogView : foodCatalogViewList) {
                    //FoodCatalogView foodCatalogView = foodCatalogViewOptional.get();
                    // view 객체에 이벤트의 eventDirectValue 를 set 함
                    foodCatalogView.setStock(catalogUpdated.getStock());
                    foodCatalogView.setName(catalogUpdated.getName());
                    foodCatalogView.setPrice(catalogUpdated.getPrice());
                    // view 레파지 토리에 save
                    foodCatalogViewRepository.save(foodCatalogView);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @StreamListener(KafkaProcessor.INPUT)
    public void whenReviewDeleted_then_DELETE_1(@Payload ReviewDeleted reviewDeleted) {
        try {
            if (reviewDeleted.isMe()) {
                // view 레파지 토리에 삭제 쿼리
                Repository.deleteById(.getId());
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }*/
}