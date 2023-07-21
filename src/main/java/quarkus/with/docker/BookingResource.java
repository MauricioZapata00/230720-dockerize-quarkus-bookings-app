package quarkus.with.docker;

import io.smallrye.mutiny.Multi;

import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.hibernate.reactive.mutiny.Mutiny;
import quarkus.with.docker.dto.BookingDto;
import quarkus.with.docker.entity.Booking;

import java.util.Objects;


@ApplicationScoped
@Path("/booking")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class BookingResource {

    @Inject
    Mutiny.SessionFactory sessionFactory;

    @GET
    public Multi<Booking> getAllBookings() {
        return sessionFactory.withSession(session ->
                session.createNamedQuery("Booking.findAll", Booking.class)
                        .getResultList())
                .toMulti().flatMap(bookings -> Multi.createFrom().iterable(bookings));
    }

    @POST
    public Uni<Response> createBooking(BookingDto bookingToCreateDto){
        Booking entityToCreate = this.validateDataToCreate(bookingToCreateDto);
        return sessionFactory.withTransaction(session -> session.persist(entityToCreate))
                .map(unused -> Response.status(Response.Status.CREATED).build())
                .onFailure().recoverWithItem(throwable -> Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(throwable.getMessage()).build());
    }

    private Booking validateDataToCreate(BookingDto booking){
        if (Objects.isNull(booking)) throw new WebApplicationException("No data attached.", 422);
        return new Booking(booking.getName(), booking.getLatitude(), booking.getLongitude(),
                booking.getDateTime());
    }

    @PUT
    @Path("/{id}")
    public Uni<Response> updateBooking(@PathParam("id") Long id, BookingDto bookingToUpdate){
        return sessionFactory.withTransaction(session -> session.find(Booking.class, id)
                .map(booking -> this.updateBookingData(booking, bookingToUpdate))
                        .flatMap(booking -> session.persist(booking)))
                .map(unused -> Response.status(Response.Status.ACCEPTED).build())
                .onFailure().recoverWithItem(throwable -> Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(throwable.getMessage()).build());
    }

    private Booking updateBookingData(Booking currentBooking, BookingDto newBooking){
        currentBooking.setName(newBooking.getName());
        currentBooking.setDateTime(newBooking.getDateTime());
        currentBooking.setLatitude(newBooking.getLatitude());
        currentBooking.setLongitude(newBooking.getLongitude());
        return currentBooking;
    }

}
