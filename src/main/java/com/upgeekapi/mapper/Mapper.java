package com.upgeekapi.mapper;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Interface genérica para o padrão Mapper (ou Converter).
 * Define um contrato padronizado para converter uma Entidade (E) em um DTO (D),
 * promovendo desacoplamento e reutilização de código.
 * @param <E> O tipo da Entidade (ex: User).
 * @param <D> O tipo do DTO (ex: UserAccountDTO).
 */
public interface Mapper<E, D> {

    /**
     * Converte uma única entidade para seu DTO correspondente.
     * @param entity A entidade a ser convertida.
     * @return O DTO resultante.
     */
    D toDto(E entity);

    /**
     * Implementação padrão para converter uma lista de entidades em uma lista de DTOs.
     * Este método 'default' evita a repetição da lógica de streaming e coleta em cada
     * implementação concreta do Mapper.
     * @param entityList A lista de entidades a ser convertida.
     * @return A lista de DTOs resultante.
     */
    default List<D> toDto(List<E> entityList) {
        return entityList.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
}