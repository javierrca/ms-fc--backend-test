package com.scmspain.services;

import com.scmspain.entities.Tweet;
import com.scmspain.entities.TweetStatus;

import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.actuate.metrics.writer.MetricWriter;

import javax.persistence.EntityManager;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class TweetServiceTest {
    private EntityManager entityManager;
    private MetricWriter metricWriter;
    private TweetService tweetService;

    @Before
    public void setUp() throws Exception {
        this.entityManager = mock(EntityManager.class);
        this.metricWriter = mock(MetricWriter.class);

        this.tweetService = new TweetService(entityManager, metricWriter);
    }

    @Test
    public void shouldInsertANewTweet() throws Exception {
        tweetService.publishTweet("Guybrush Threepwood", "I am Guybrush Threepwood, mighty pirate.");

        verify(entityManager).persist(any(Tweet.class));
    }
    
    
    @Test
    public void shouldDiscardATweet() throws Exception {
    	
        tweetService.publishTweet("Guybrush Threepwood", "I am Guybrush Threepwood, mighty pirate.");
        verify(entityManager).persist(any(Tweet.class));
        
        Long id = new Long(1);
   
        tweetService.discardTweet(id);
        verify(entityManager).persist(any(Tweet.class));
        verify(entityManager).find(Tweet.class,id);
        
        
    }    

    @Test
    public void shouldInsertAValidTweetWithExceededSizeByLinks() throws Exception {
        tweetService.publishTweet("Pirate","We are Schibsted Spain (look at our home page http://www.schibsted.es/), we own Vibbo, InfoJobs, fotocasa, coches.net and milanuncios. Welcome!");

        verify(entityManager).persist(any(Tweet.class));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowAnExceptionWhenTweetLengthIsInvalid() throws Exception {
        tweetService.publishTweet("Pirate", "LeChuck? He's the guy that went to the Governor's for dinner and never wanted to leave. He fell for her in a big way, but she told him to drop dead. So he did. Then things really got ugly.");
    }
}
