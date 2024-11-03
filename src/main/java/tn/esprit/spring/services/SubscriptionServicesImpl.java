package tn.esprit.spring.services;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import tn.esprit.spring.entities.Skier;
import tn.esprit.spring.entities.Subscription;
import tn.esprit.spring.entities.TypeSubscription;
import tn.esprit.spring.repositories.ISkierRepository;
import tn.esprit.spring.repositories.ISubscriptionRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Slf4j
@AllArgsConstructor
@Service
public class SubscriptionServicesImpl implements ISubscriptionServices{

    private ISubscriptionRepository subscriptionRepository;

    private ISkierRepository skierRepository;

    @Override
    public Subscription addSubscription(Subscription subscription) {
        switch (subscription.getTypeSub()) {
            case ANNUAL:
                subscription.setEndDate(subscription.getStartDate().plusYears(1));
                break;
            case SEMESTRIEL:
                subscription.setEndDate(subscription.getStartDate().plusMonths(6));
                break;
            case MONTHLY:
                subscription.setEndDate(subscription.getStartDate().plusMonths(1));
                break;
        }
        return subscriptionRepository.save(subscription);
    }

    @Override
    public Subscription updateSubscription(Subscription subscription) {
        return subscriptionRepository.save(subscription);
    }

    @Override
    public Subscription retrieveSubscriptionById(Long numSubscription) {
        return subscriptionRepository.findById(numSubscription).orElse(null);
    }

    @Override
    public Set<Subscription> getSubscriptionByType(TypeSubscription type) {
        return subscriptionRepository.findByTypeSubOrderByStartDateAsc(type);
    }

    @Override
    public List<Subscription> retrieveSubscriptionsByDates(LocalDate startDate, LocalDate endDate) {
        return subscriptionRepository.getSubscriptionsByStartDateBetween(startDate, endDate);
    }

    @Override
    @Scheduled(cron = "*/30 * * * * *") /* Cron expression to run a job every 30 secondes */
    public void retrieveSubscriptions() {
        for (Subscription sub: subscriptionRepository.findDistinctOrderByEndDateAsc()) {
            Skier   aSkier = skierRepository.findBySubscription(sub);
            log.info(sub.getNumSub().toString() + " | "+ sub.getEndDate().toString()
                    + " | "+ aSkier.getFirstName() + " " + aSkier.getLastName());
        }
    }
    //changed it to count in NULL values in revenues
   // @Scheduled(cron = "* 0 9 1 * *") /* Cron expression to run a job every month at 9am */
   @Scheduled(cron = "*/30 * * * * *") /* Cron expression to run a job every 30 seconds */
   public Float showMonthlyRecurringRevenue() {
       // Handle potential null values directly in the calculation
       Float monthlyRevenue = subscriptionRepository.recurringRevenueByTypeSubEquals(TypeSubscription.MONTHLY);
       if (monthlyRevenue == null) {
           monthlyRevenue = 0.0f;
       }

       Float semestrialRevenue = subscriptionRepository.recurringRevenueByTypeSubEquals(TypeSubscription.SEMESTRIEL);
       if (semestrialRevenue == null) {
           semestrialRevenue = 0.0f;
       }

       Float annualRevenue = subscriptionRepository.recurringRevenueByTypeSubEquals(TypeSubscription.ANNUAL);
       if (annualRevenue == null) {
           annualRevenue = 0.0f;
       }

       Float revenue = monthlyRevenue + (semestrialRevenue / 6) + (annualRevenue / 12);
       log.info("Monthly Revenue = " + revenue);
       return revenue;
   }

}
