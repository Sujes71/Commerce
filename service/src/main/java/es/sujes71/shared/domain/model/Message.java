package es.sujes71.shared.domain.model;

public record Message<B>(String address, B body) { }
