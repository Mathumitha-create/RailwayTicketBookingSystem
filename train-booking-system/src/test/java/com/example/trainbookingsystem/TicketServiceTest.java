package com.example.trainbookingsystem;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.trainbookingsystem.entity.Ticket;
import com.example.trainbookingsystem.entity.Train;
import com.example.trainbookingsystem.entity.User;
import com.example.trainbookingsystem.repository.TicketRepository;
import com.example.trainbookingsystem.repository.TrainRepository;
import com.example.trainbookingsystem.repository.UserRepository;

import com.example.trainbookingsystem.service.TicketService;

@ExtendWith(MockitoExtension.class)
public class TicketServiceTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private TrainRepository trainRepository;

    @Mock
    private TicketRepository ticketRepository;

    @InjectMocks
    private TicketService trainService;

    private Ticket ticket;
    private User user;
    private Train train;

    @BeforeEach
    void setUp() {
        user=new User();
        user.setId(1L);
        user.setName("Mathumitha");
        user.setEmail("mathu@gmail.com");

        train = new Train();
        train.setId(1L);
        train.setName("Chennai Express");
        train.setSource("Coimbatore");
        train.setDestination("Chennai");
        
        ticket =new Ticket();
        ticket.setId(1L);
        ticket.setUser(user);
        ticket    }

    @Test
    void getAllTrains_ShouldReturnListOfTrains() {
        List<Train> trains = Arrays.asList(train);
        when(trainRepository.findAll()).thenReturn(trains);

        List<Train> result = trainService.getAllTrains();

        assertEquals(1, result.size());
        assertEquals(train.getName(), result.get(0).getName());
        verify(trainRepository, times(1)).findAll();
    }

    @Test
    void getTrainById_WhenTrainExists_ShouldReturnTrain() {
        when(trainRepository.findById(1L)).thenReturn(Optional.of(train));

        Optional<Train> result = trainService.getTrainById(1L);

        assertTrue(result.isPresent());
        assertEquals(train.getDestination(), result.get().getDestination());
        verify(trainRepository, times(1)).findById(1L);
    }

    @Test
    void getTrainById_WhenTrainDoesNotExist_ShouldReturnEmpty() {
        when(trainRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<Train> result = trainService.getTrainById(1L);

        assertFalse(result.isPresent());
        verify(trainRepository, times(1)).findById(1L);
    }

    @Test
    void createTrain_ShouldSaveAndReturnTrain() {
        when(trainRepository.save(any(Train.class))).thenReturn(train);

        Train result = trainService.createTrain(train);

        assertNotNull(result);
        assertEquals(train.getName(), result.getName());
        verify(trainRepository, times(1)).save(train);
    }

    @Test
    void updateTrain_WhenTrainExists_ShouldUpdateAndReturnTrain() {
        Train updatedTrain = new Train();
        updatedTrain.setName("Super Express");
        updatedTrain.setSource("Chennai");
        updatedTrain.setDestination("Mumbai");
        updatedTrain.setBasePrice(2000.0);
        updatedTrain.setDiscountPercentage(15.0);

        when(trainRepository.findById(1L)).thenReturn(Optional.of(train));
        when(trainRepository.save(any(Train.class))).thenReturn(train);

        Train result = trainService.updateTrain(1L, updatedTrain);

        assertEquals(updatedTrain.getName(), result.getName());
        assertEquals(updatedTrain.getDestination(), result.getDestination());
        assertEquals(updatedTrain.getBasePrice(), result.getBasePrice());
        assertEquals(updatedTrain.getDiscountPercentage(), result.getDiscountPercentage());
        verify(trainRepository, times(1)).findById(1L);
        verify(trainRepository, times(1)).save(train);
    }

    @Test
    void updateTrain_WhenTrainDoesNotExist_ShouldThrowException() {
        when(trainRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(Exception.class, () -> trainService.updateTrain(1L, train));
        verify(trainRepository, times(1)).findById(1L);
        verify(trainRepository, never()).save(any(Train.class));
    }

    @Test
    void deleteTrain_ShouldDeleteTrain() {
        doNothing().when(trainRepository).deleteById(1L);

        trainService.deleteTrain(1L);

        verify(trainRepository, times(1)).deleteById(1L);
    }
}
