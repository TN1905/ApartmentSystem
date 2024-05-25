package com.poly.dao;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.poly.entity.ApartStatus;
import com.poly.entity.HistoryRented;
import com.poly.entity.RentApartmentDetail;
import com.poly.entity.Rented;
import com.poly.entity.Revenue;

public interface RentApartmentDetailDAO extends JpaRepository<RentApartmentDetail,Integer>{
	@Query("SELECT new Revenue(o.apartment.id, o.apartment.content, o.rentApartment.account.username, sum(o.price)) "
	        + "FROM RentApartmentDetail o "
	        + "GROUP BY o.apartment.id, o.apartment.content, o.rentApartment.account.username "
	        + "ORDER BY sum(o.price) DESC")
	List<Revenue> getRevenue();
	
	@Query("SELECT new Rented(o.apartment.id, o.apartment.content, o.rentApartment.account.username,o.rentApartment.createdate,o.rentApartment.enddate) "
	        + "FROM RentApartmentDetail o")
	List<Rented> getRented();
	
	@Query("SELECT new ApartStatus(o.apartment.id, o.apartment.content, o.apartment.status) "
	        + "FROM RentApartmentDetail o")
	List<ApartStatus> getStatus();
	
	@Query("SELECT new HistoryRented(o.apartment.id, o.apartment.content, o.rentApartment.account.username, o.price,o.rentApartment.monthrent, o.rentApartment.createdate, o.rentApartment.enddate) "
	        + "FROM RentApartmentDetail o WHERE o.rentApartment.account.username = ?1")
	List<HistoryRented> getHistoryRent(String username);

	//gửi email khi hợp đồng thuê sắp hết hạn
	@Query("SELECT new Rented(o.apartment.id, o.apartment.content, o.rentApartment.account.username, o.rentApartment.createdate, o.rentApartment.enddate) " +
		       "FROM RentApartmentDetail o " +
		       "WHERE o.rentApartment.enddate BETWEEN :startDate AND :endDate")
		List<Rented> getRentedWithEndDate(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
	//thống kê doanh thu theo tháng
	 @Query("SELECT new Revenue(o.apartment.id, o.apartment.content, o.rentApartment.account.username, SUM(o.price), MONTH(o.rentApartment.createdate)) "
	            + "FROM RentApartmentDetail o "
	            + "GROUP BY o.apartment.id, o.apartment.content, o.rentApartment.account.username, MONTH(o.rentApartment.createdate) "
	            + "ORDER BY SUM(o.price) DESC")
	    List<Revenue> getMonthlyRevenue();
}
