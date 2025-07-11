package com.loca_mais.backend.service;

import com.loca_mais.backend.dao.AdvertisementDAO;
import com.loca_mais.backend.dto.create.AdvertisementCreateDTO;
import com.loca_mais.backend.dto.response.AdvertisementResponse;
import com.loca_mais.backend.exceptions.custom.core.Advertisement.AdvertisementImagesException;
import com.loca_mais.backend.exceptions.custom.core.EntityNotFoundException;
import com.loca_mais.backend.mappers.AdvertisementMapper;
import com.loca_mais.backend.model.AdvertisementEntity;
import com.loca_mais.backend.model.PropertyEntity;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@AllArgsConstructor
public class AdvertisementService {

    private final AdvertisementImagesService advertisementImagesService;
    private final AdvertisementDAO  advertisementDAO;
    private final AdvertisementMapper  advertisementMapper;
    private final PropertyService propertyService;

    public void save(AdvertisementCreateDTO createDTO,MultipartFile[] advertisementImage) throws IOException {
        propertyService.findById(createDTO.propertyId()).orElseThrow(()->new EntityNotFoundException("Propriedade não encontrada"));
        try{
            AdvertisementEntity advertisementEntity = advertisementMapper.toEntity(createDTO);
            advertisementDAO.save(advertisementEntity);
        advertisementImagesService.
                save(advertisementImage, advertisementEntity.getId());
        }catch(IOException e){
            throw new AdvertisementImagesException();
        }
    }

    public AdvertisementResponse findById(Integer id){
        return advertisementDAO.findById(id).orElseThrow(()->new EntityNotFoundException("Anúncio não encontrado"));

    }

    public List<AdvertisementResponse> findAll(){
         return advertisementDAO.findAll();
    }

    public List<AdvertisementResponse> findAllByQuery(String query){
        return advertisementDAO.findAllByQuery(query);
    }

    public void update(AdvertisementCreateDTO createDTO,Integer id,MultipartFile[] advertisementImage) throws IOException {
        AdvertisementEntity advertisement = advertisementMapper.toEntity(createDTO);
        if(advertisementImage!=null && advertisementImage.length >0 ){
        advertisementImagesService.save(advertisementImage, id);
        }
        advertisementDAO.updateAdvertisementPartial(advertisement,id);
    }

    public void delete(Integer advertisementId){
        advertisementImagesService.delete(advertisementId);
        advertisementDAO.delete(advertisementId);

    }

    public void deleteImages(Integer id,String imageUrl){
        advertisementImagesService.deleteByUrlAndAdvertisementId(imageUrl,id);
    }






}
