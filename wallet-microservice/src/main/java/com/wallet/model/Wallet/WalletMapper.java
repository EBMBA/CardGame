package com.wallet.model.Wallet;

import org.mapstruct.Mapper;

import com.example.common.model.WalletDTO;

@Mapper(componentModel = "spring")
public interface WalletMapper {
    Wallet toEntity(WalletDTO model);
    WalletDTO toEntity(Wallet model);
}
