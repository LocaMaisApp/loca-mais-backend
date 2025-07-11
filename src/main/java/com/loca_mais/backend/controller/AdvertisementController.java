package com.loca_mais.backend.controller;

import com.loca_mais.backend.dto.create.AdvertisementCreateDTO;
import com.loca_mais.backend.dto.response.AdvertisementResponse;
import com.loca_mais.backend.service.AdvertisementService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/advertisement")
@AllArgsConstructor
public class AdvertisementController {

    private final AdvertisementService advertisementService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<AdvertisementResponse> createAdvertisement(
            @RequestParam("description") String description,
            @RequestParam("condominiumValue") Double condominiumValue,
            @RequestParam("iptuValue") Double iptuValue,
            @RequestParam("value") Double value,
            @RequestParam("propertyId") Integer propertyId,
            @RequestPart(value = "images", required = false) MultipartFile[] images) throws IOException  {
        advertisementService.save(new AdvertisementCreateDTO(description,condominiumValue,value,iptuValue,propertyId),images);
        return ResponseEntity.status(HttpStatus.CREATED).build();

    }

    @GetMapping()
    public ResponseEntity<List<AdvertisementResponse>> findAll(){
        List<AdvertisementResponse> advertisementResponses=advertisementService.findAll();
        return  ResponseEntity.status(HttpStatus.OK).body(advertisementResponses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AdvertisementResponse> findById(@PathVariable("id") Integer propertyId){
        AdvertisementResponse advertisementResponse = advertisementService.findById(propertyId);
        return ResponseEntity.status(HttpStatus.OK).body(advertisementResponse);
    }

    @GetMapping("/search")
    public ResponseEntity<List<AdvertisementResponse>> findAllByQuery(@RequestParam("query") String query){
        List<AdvertisementResponse> advertisementResponses=advertisementService.findAllByQuery(query);
        return  ResponseEntity.status(HttpStatus.OK).body(advertisementResponses);
    }

    @PatchMapping(value = "/{id}",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> update(
            @RequestParam("description") String description,
            @RequestParam("condominiumValue") Double condominiumValue,
            @RequestParam("iptuValue") Double iptuValue,
            @RequestParam("value") Double value,
            @RequestPart(value = "images", required = false) MultipartFile[] images,
            @PathVariable("id") Integer id
    ) throws IOException{
        AdvertisementCreateDTO advertisementCreateDTO=new AdvertisementCreateDTO(
                description,
                condominiumValue,
                value,
                iptuValue,
                null
        );
        advertisementService.update(advertisementCreateDTO,id,images);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Integer id){
        advertisementService.delete(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping("/{id}/images")
    public ResponseEntity<Void> deleteAdvertisementImages(@PathVariable("id") Integer id, @RequestParam("imageUrl") String imageUrl){
        advertisementService.deleteImages(id,imageUrl);
        return ResponseEntity.status(HttpStatus.OK).build();
    }




}
