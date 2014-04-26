package com.ssbusy.rating.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.beanutils.BeanComparator;
import org.broadleafcommerce.common.time.SystemTime;
import org.broadleafcommerce.core.rating.dao.RatingSummaryDao;
import org.broadleafcommerce.core.rating.dao.ReviewDetailDao;
import org.broadleafcommerce.core.rating.domain.RatingDetail;
import org.broadleafcommerce.core.rating.domain.RatingDetailImpl;
import org.broadleafcommerce.core.rating.domain.RatingSummary;
import org.broadleafcommerce.core.rating.domain.RatingSummaryImpl;
import org.broadleafcommerce.core.rating.domain.ReviewDetail;
import org.broadleafcommerce.core.rating.domain.ReviewDetailImpl;
import org.broadleafcommerce.core.rating.domain.ReviewFeedback;
import org.broadleafcommerce.core.rating.service.type.RatingSortType;
import org.broadleafcommerce.core.rating.service.type.RatingType;
import org.broadleafcommerce.profile.core.domain.Customer;
import org.springframework.transaction.annotation.Transactional;

public class MyRatingServiceImpl implements MyRatingService{

	    @Resource(name="blRatingSummaryDao")
	    protected RatingSummaryDao ratingSummaryDao;

	    @Resource(name="blReviewDetailDao")
	    protected ReviewDetailDao reviewDetailDao;

	    @Transactional("blTransactionManager")
	    public void deleteRatingSummary(RatingSummary ratingSummary) {
	        ratingSummaryDao.deleteRatingSummary(ratingSummary);
	    }

	    @Transactional("blTransactionManager")
	    public void markReviewHelpful(Long reviewId, Customer customer, Boolean helpful) {
	        ReviewDetail reviewDetail = reviewDetailDao.readReviewDetailById(reviewId);

	        if (reviewDetail != null) {
	            ReviewFeedback reviewFeedback = reviewDetailDao.createFeedback();
	            reviewFeedback.setCustomer(customer);
	            reviewFeedback.setIsHelpful(helpful);
	            reviewFeedback.setReviewDetail(reviewDetail);
	            reviewDetail.getReviewFeedback().add(reviewFeedback);
	            reviewDetailDao.saveReviewDetail(reviewDetail);
	        }

	    }

	    @Transactional("blTransactionManager")
	    public void rateItem(String itemId, RatingType type, Customer customer, Double rating) {
	        RatingSummary ratingSummary = this.readRatingSummary(itemId, type);

	        if (ratingSummary == null) {
	            ratingSummary = new RatingSummaryImpl(itemId, type);
	        }

	        RatingDetail ratingDetail = ratingSummaryDao.readRating(customer.getId(), ratingSummary.getId());

	        if (ratingDetail == null) {
	            ratingDetail = new RatingDetailImpl(ratingSummary, rating, SystemTime.asDate(), customer);
	        }

	        ratingDetail.setRating(rating);

	        ratingSummary.getRatings().add(ratingDetail);
	        ratingSummaryDao.saveRatingSummary(ratingSummary);
	    }

	    public RatingSummary readRatingSummary(String itemId, RatingType type) {
	        return ratingSummaryDao.readRatingSummary(itemId, type);
	    }

	    public Map<String, RatingSummary> readRatingSummaries(List<String> itemIds, RatingType type) {
	        List<RatingSummary> ratings = ratingSummaryDao.readRatingSummaries(itemIds, type);
	        Map<String, RatingSummary> ratingsMap = new HashMap<String, RatingSummary>();

	        for (RatingSummary ratingSummary : ratings) {
	            ratingsMap.put(ratingSummary.getItemId(), ratingSummary);
	        }

	        return ratingsMap;
	    }

	    @SuppressWarnings("unchecked")
	    public List<ReviewDetail> readReviews(String itemId, RatingType type, int start, int finish, RatingSortType sortBy) {
	        RatingSummary summary = this.readRatingSummary(itemId, type);
	        if(summary == null)
	        	return Collections.emptyList();
	        List<ReviewDetail> reviews = summary.getReviews();
	        List<ReviewDetail> reviewsToReturn = new ArrayList<ReviewDetail>();
	        int i = 0;
	        for (ReviewDetail review : reviews) {
	            if (i > finish) {
	                break;
	            }

	            if (i >= start) {
	                reviewsToReturn.add(review);
	            }

	            i++;
	        }

	        String sortByBeanProperty = "reviewSubmittedDate";
	        if (sortBy == RatingSortType.MOST_HELPFUL) {
	            sortByBeanProperty = "helpfulCount";
	        }

	        Collections.sort(reviewsToReturn, new BeanComparator(sortByBeanProperty));

	        return reviewsToReturn;
	    }

	    @Transactional("blTransactionManager")
	    public RatingSummary saveRatingSummary(RatingSummary ratingSummary) {
	        return ratingSummaryDao.saveRatingSummary(ratingSummary);
	    }

	    @Transactional("blTransactionManager")
	    public void reviewItem(String itemId, RatingType type, Customer customer, Double rating, String reviewText) {
	        RatingSummary ratingSummary = this.readRatingSummary(itemId, type);

	        if (ratingSummary == null) {
	            ratingSummary = new RatingSummaryImpl(itemId, type);
	        }

	        RatingDetail ratingDetail = ratingSummaryDao.readRating(customer.getId(), ratingSummary.getId());

	        if (ratingDetail == null) {
	            ratingDetail = new RatingDetailImpl(ratingSummary, rating, SystemTime.asDate(), customer);
	        } else {
	            ratingDetail.setRating(rating);         
	        }

	        ratingSummary.getRatings().add(ratingDetail);

	        ReviewDetail reviewDetail = ratingSummaryDao.readReview(customer.getId(), ratingSummary.getId());

	        if (reviewDetail == null) {
	            reviewDetail = new ReviewDetailImpl(customer, SystemTime.asDate(), ratingDetail, reviewText, ratingSummary);
	            ratingSummary.getReviews().add(reviewDetail);
	        } else {
	            reviewDetail.setReviewText(reviewText);         
	        }

	       // 
	        // load reviews
	        ratingSummary.getReviews().size();
	        ratingSummaryDao.saveRatingSummary(ratingSummary);
	    }
	    
	    @Override
	    public ReviewDetail readReviewByCustomerAndItem(Customer customer, String itemId) {
	        return reviewDetailDao.readReviewByCustomerAndItem(customer, itemId);
	    }

	}
