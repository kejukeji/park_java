package org.broadleafcommerce.core.rating.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.beanutils.BeanComparator;
import org.broadleafcommerce.core.rating.domain.RatingSummary;
import org.broadleafcommerce.core.rating.domain.ReviewDetail;
import org.broadleafcommerce.core.rating.service.type.RatingSortType;
import org.broadleafcommerce.core.rating.service.type.RatingType;

public class RatingServiceImplEx extends RatingServiceImpl {

	@SuppressWarnings("unchecked")
	@Override
	public List<ReviewDetail> readReviews(String itemId, RatingType type,
			int start, int finish, RatingSortType sortBy) {
        RatingSummary summary = this.readRatingSummary(itemId, type);
        if(summary == null)
        	return Collections.emptyList();

        List<ReviewDetail> reviews = summary.getReviews();
        List<ReviewDetail> reviewsToReturn = new ArrayList<ReviewDetail>();
        int i = 0;
        for (ReviewDetail review : reviews) {
            if (i > finish) { // FIXME performance
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

}
