package com.mapbox.services.api.directionsmatrix.v1;

import com.mapbox.services.api.MapboxBuilder;
import com.mapbox.services.api.MapboxService;
import com.mapbox.services.api.ServicesException;
import com.mapbox.services.api.directions.v5.DirectionsCriteria;
import com.mapbox.services.api.directionsmatrix.v1.models.DirectionsMatrixResponse;
import com.mapbox.services.commons.models.Position;
import com.mapbox.services.commons.utils.TextUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MapboxDirectionsMatrix extends MapboxService<DirectionsMatrixResponse> {

  protected Builder builder = null;
  private DirectionsMatrixService service = null;
  private Call<DirectionsMatrixResponse> call = null;

  protected MapboxDirectionsMatrix(Builder builder) {
    this.builder = builder;
  }

  private DirectionsMatrixService getService() {
    // No need to recreate it
    if (service != null) {
      return service;
    }

    // Retrofit instance
    Retrofit.Builder retrofitBuilder = new Retrofit.Builder()
      .baseUrl(builder.getBaseUrl())
      .addConverterFactory(GsonConverterFactory.create());
    if (getCallFactory() != null) {
      retrofitBuilder.callFactory(getCallFactory());
    } else {
      retrofitBuilder.client(getOkHttpClient());
    }

    // Directions service
    service = retrofitBuilder.build().create(DirectionsMatrixService.class);
    return service;
  }

  private Call<DirectionsMatrixResponse> getCall() {
    // No need to recreate it
    if (call != null) {
      return call;
    }

    call = getService().getCall(
      getHeaderUserAgent(builder.getClientAppName()),
      builder.getUser(),
      builder.getProfile(),
      builder.getCoordinates(),
      builder.getAccessToken(),
      builder.getDestinations(),
      builder.getSources());

    // Done
    return call;
  }

  /**
   * Execute the call
   *
   * @return The Directions v5 response
   * @throws IOException Signals that an I/O exception of some sort has occurred.
   * @since 2.1.0
   */
  @Override
  public Response<DirectionsMatrixResponse> executeCall() throws IOException {
    return getCall().execute();
  }

  /**
   * Execute the call
   *
   * @param callback A Retrofit callback.
   * @since 2.1.0
   */
  @Override
  public void enqueueCall(Callback<DirectionsMatrixResponse> callback) {
    getCall().enqueue(callback);
  }

  /**
   * Cancel the call
   *
   * @since 2.1.0
   */
  @Override
  public void cancelCall() {
    getCall().cancel();
  }

  /**
   * clone the call
   *
   * @return cloned call
   * @since 2.1.0
   */
  @Override
  public Call<DirectionsMatrixResponse> cloneCall() {
    return getCall().clone();
  }


  public static class Builder<T extends Builder> extends MapboxBuilder {

    private String user = null;
    private List<Position> coordinates = null;
    private String accessToken = null;
    private String profile = null;
    private int[] sources = null;
    private int[] destinations = null;

    /**
     * Constructor
     *
     * @since 1.0.0
     */
    public Builder() {
      // Set defaults
      this.user = DirectionsCriteria.PROFILE_DEFAULT_USER;
    }

    /*
     * Setters
     */

    public T setDestinations(int... destinations) {
      this.destinations = destinations;
      return (T) this;
    }

    public T setSources(int... sources) {
      this.sources = sources;
      return (T) this;
    }

    public T setOrigin(Position origin) {
      if (coordinates == null) {
        coordinates = new ArrayList<>();
      }

      // The default behavior of ArrayList is to inserts the specified element at the
      // specified position in this list (beginning) and to shift the element currently at
      // that position (if any) and any subsequent elements to the right (adds one to
      // their indices)
      coordinates.add(0, origin);

      return (T) this;
    }

    public T setDestination(Position destination) {
      if (coordinates == null) {
        coordinates = new ArrayList<>();
      }

      // The default behavior for ArrayList is to appends the specified element
      // to the end of this list.
      coordinates.add(destination);

      return (T) this;
    }

    public T setCoordinates(List<Position> coordinates) {
      this.coordinates = coordinates;
      return (T) this;
    }

    public T setUser(String user) {
      this.user = user;
      return (T) this;
    }

    public T setProfile(String profile) {
      this.profile = profile;
      return (T) this;
    }

    @Override
    public T setAccessToken(String accessToken) {
      this.accessToken = accessToken;
      return (T) this;
    }

    public T setBaseUrl(String baseUrl) {
      super.baseUrl = baseUrl;
      return (T) this;
    }

    public T setClientAppName(String appName) {
      super.clientAppName = appName;
      return (T) this;
    }

    /*
     * Getters
     */


    public String getDestinations() {
      if (destinations == null || destinations.length == 0) {
        return null;
      }

      String[] destinationsFormatted = new String[destinations.length];
      return TextUtils.join(";", destinationsFormatted);
    }

    public String getSources() {
      if (sources == null || sources.length == 0) {
        return null;
      }

      String[] sourcesFormatted = new String[sources.length];
      return TextUtils.join(";", sourcesFormatted);
    }

    public String getCoordinates() {
      List<String> coordinatesFormatted = new ArrayList<>();
      for (Position coordinate : coordinates) {
        coordinatesFormatted.add(String.format(Locale.US, "%f,%f",
          coordinate.getLongitude(),
          coordinate.getLatitude()));
      }

      return TextUtils.join(";", coordinatesFormatted.toArray());
    }

    public String getUser() {
      return user;
    }

    public String getProfile() {
      return profile;
    }

    @Override
    public String getAccessToken() {
      return accessToken;
    }

    @Override
    public MapboxDirectionsMatrix build() throws ServicesException {
      validateAccessToken(accessToken);

      if (profile == null) {
        throw new ServicesException(
          "A profile is required for the Directions Matrix API. Use one of the profiles found in the"
            + "DirectionsCriteria.java file.");
      }

      if (coordinates == null || coordinates.size() < 2) {
        throw new ServicesException(
          "You should provide at least two coordinates (from/to).");
      }

      if (profile.equals(DirectionsCriteria.PROFILE_DRIVING_TRAFFIC)) {
        throw new ServicesException(
          "The directions matrix doesn't support the driving traffic profile.");
      }

      if (coordinates.size() > 25) {
        throw new ServicesException(
          "All profiles allows for maximum of 25 coordinates.");
      }

      return new MapboxDirectionsMatrix(this);
    }
  }
}
