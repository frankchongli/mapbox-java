package com.mapbox.services.api.directionsmatrix.v1.models;

import com.mapbox.services.api.directions.v5.models.DirectionsWaypoint;

import java.util.List;

public class DirectionsMatrixResponse {

  private String code;
  private double[][] durations;
  private List<DirectionsWaypoint> destinations;
  private List<DirectionsWaypoint> sources;

  public DirectionsMatrixResponse() {
  }

  /**
   * String indicating the state of the response. This is a separate code than the HTTP status code. On normal valid
   * responses, the value will be Ok. See the errors section below for more information.
   *
   * @return "Ok", "NoRoute", "ProfileNotFound", or "InvalidInput".
   * @since 2.1.0
   */
  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public double[][] getDurations() {
    return durations;
  }

  public void setDurations(double[][] durations) {
    this.durations = durations;
  }

  public List<DirectionsWaypoint> getDestinations() {
    return destinations;
  }

  public void setDestinations(List<DirectionsWaypoint> destinations) {
    this.destinations = destinations;
  }

  public List<DirectionsWaypoint> getSources() {
    return sources;
  }

  public void setSources(List<DirectionsWaypoint> sources) {
    this.sources = sources;
  }

}
