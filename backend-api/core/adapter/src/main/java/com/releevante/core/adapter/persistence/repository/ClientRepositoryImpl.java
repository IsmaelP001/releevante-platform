package com.releevante.core.adapter.persistence.repository;

import com.releevante.core.adapter.persistence.dao.*;
import com.releevante.core.adapter.persistence.records.*;
import com.releevante.core.domain.Client;
import com.releevante.core.domain.ClientId;
import com.releevante.core.domain.LoanDetail;
import com.releevante.core.domain.repository.CartRepository;
import com.releevante.core.domain.repository.ClientRepository;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Component
public class ClientRepositoryImpl implements ClientRepository {

  final ClientHibernateDao clientHibernateDao;

  final CartRepository cartRepository;

  final ServiceRatingHibernateDao serviceRatingHibernateDao;

  final BookLoanHibernateDao bookLoanHibernateDao;

  final BookRatingHibernateDao bookRatingHibernateDao;

  final BookSaleHibernateDao bookSaleHibernateDao;

  public ClientRepositoryImpl(
      ClientHibernateDao clientHibernateDao,
      CartRepository cartRepository,
      ServiceRatingHibernateDao serviceRatingHibernateDao,
      BookLoanHibernateDao bookLoanHibernateDao,
      BookRatingHibernateDao bookRatingHibernateDao,
      BookSaleHibernateDao bookSaleHibernateDao) {
    this.clientHibernateDao = clientHibernateDao;
    this.cartRepository = cartRepository;
    this.serviceRatingHibernateDao = serviceRatingHibernateDao;
    this.bookLoanHibernateDao = bookLoanHibernateDao;
    this.bookRatingHibernateDao = bookRatingHibernateDao;
    this.bookSaleHibernateDao = bookSaleHibernateDao;
  }

  @Override
  public Mono<Client> find(ClientId clientId) {
    return Mono.justOrEmpty(clientHibernateDao.findById(clientId.value()))
        .map(ClientRecord::toDomain);
  }

  @Override
  public Mono<Client> saveServiceRating(Client client) {
    return ClientRecord.serviceRating(client).map(clientHibernateDao::save).thenReturn(client);
  }

  @Override
  public Mono<Client> saveBookLoan(Client client) {
    return ClientRecord.bookLoans(client)
        .map(clientHibernateDao::save)
        .thenReturn(client)
        .defaultIfEmpty(client);
  }

  @Override
  public Mono<Client> saveBookRating(Client client) {
    return ClientRecord.bookRatings(client)
        .map(clientHibernateDao::save)
        .thenReturn(client)
        .defaultIfEmpty(client);
  }

  @Override
  public Mono<Client> savePurchase(Client client) {
    return ClientRecord.bookPurchases(client)
        .map(clientHibernateDao::save)
        .thenReturn(client)
        .defaultIfEmpty(client);
  }

  @Override
  public Mono<Client> saveReservations(Client client) {
    return ClientRecord.bookReservations(client)
        .map(clientHibernateDao::save)
        .thenReturn(client)
        .defaultIfEmpty(client);
  }

  @Override
  public Mono<Client> saveCarts(Client client) {
    return ClientRecord.carts(client)
        .map(clientHibernateDao::save)
        .thenReturn(client)
        .defaultIfEmpty(client);
  }

  @Transactional
  @Override
  public Mono<Client> synchronize(Client client) {

    client.loans().get().stream()
        .flatMap(loan -> loan.loanDetails().get().stream())
        .map(LoanDetail::bookCopy)
        .collect(Collectors.toSet());

    return Mono.zip(saveCarts(client), saveBookLoan(client)).thenReturn(client);
  }
}
