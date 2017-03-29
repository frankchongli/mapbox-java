package com.mapbox.services.api.directionsmatrix.v1;

import com.mapbox.services.api.MapboxBuilder;
import com.mapbox.services.api.ServicesException;
import com.mapbox.services.api.directions.v5.DirectionsCriteria;
import com.mapbox.services.commons.models.Position;
import com.mapbox.services.commons.utils.TextUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MapboxDirectionsMatrix {


  protected MapboxDirectionsMatrix(Builder builder) {
    this.builder = builder;
  }


  




















  public static class Builder<T extends Builder> extends MapboxBuilder {

    private String user = null;
    private List<Position> coordinates = null;
    private String accessToken = null;
    private String profile = null;
    private int[] sources = null;
    private int[] destinations = null;

    public String getDestinations() {
      if (destinations == null || destinations.length == 0) {
        return null;
      }

      String[] destinationsFormatted = new String[destinations.length];
      return TextUtils.join(";", destinationsFormatted);
    }

    public T setDestinations(int... destinations) {
      this.destinations = destinations;
      return (T) this;
    }

    public String getSources() {
      if (sources == null || sources.length == 0) {
        return null;
      }

      String[] sourcesFormatted = new String[sources.length];
      return TextUtils.join(";", sourcesFormatted);
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

    public List<Position> getCoordinates() {
      return coordinates;
    }

    public void setCoordinates(List<Position> coordinates) {
      this.coordinates = coordinates;
    }

    public String getUser() {
      return user;
    }

    public void setUser(String user) {
      this.user = user;
    }

    public String getProfile() {
      return profile;
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

    @Override
    public String getAccessToken() {
      return accessToken;
    }

    public T setBaseUrl(String baseUrl) {
      super.baseUrl = baseUrl;
      return (T) this;
    }

    public T setClientAppName(String appName) {
      super.clientAppName = appName;
      return (T) this;
    }

    @Override
    public Object build() throws ServicesException {
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
