package com.loca_mais.backend.exceptions.custom.core.Advertisement;

import com.loca_mais.backend.exceptions.core.ApiException;
import org.springframework.http.HttpStatus;

public class AdvertisementImagesException extends ApiException {
    public AdvertisementImagesException(){
        super(HttpStatus.INTERNAL_SERVER_ERROR,"Erro ao salvar imagens");
    }
}
