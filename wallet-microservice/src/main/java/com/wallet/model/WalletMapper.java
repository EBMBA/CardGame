package com.wallet.model;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.example.common.model.WalletDTO;

@Mapper(componentModel = "spring")
public interface WalletMapper {
    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "wallet_id", ignore = true)
    Wallet toEntity(WalletDTO model);
    
    @Mapping(target = "user", ignore = true)
    WalletDTO toDTO(Wallet model); 
}
