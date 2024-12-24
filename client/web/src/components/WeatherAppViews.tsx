import React from "react";
import { useWeatherApp } from "../hook/useWeatherApp";
import LoadingSpinner from "../utils/LoadingSpinner";

const WeatherAppViews: React.FC = () => {
   const {
        location,
        setLocation,
        weatherdata,
        error,
        loading, 
        handleSubmit
   } = useWeatherApp();

    return (
        <section className="container forms">
            <div className="form weather-link">
                <div className="form-content">
                    <header></header>
                    <form id="weather-form" onSubmit={handleSubmit}>
                        <div className="search-box">
                            <input
                                type="text"
                                name="location"
                                placeholder="Enter your location"
                                value={location}
                                onChange={(e) => setLocation(e.target.value)}
                            />
                            <button className="search-button" type="submit">
                                <img src="search.png" alt="search" />
                            </button>
                        </div>
                        <div className="weather-box">
                            {loading ? (
                                <LoadingSpinner />
                            ) : (
                                <>
                                    {weatherdata.icon && (
                                        <div className="main-right">
                                        <>
                                        <img className="imgWeather" src={`https://openweathermap.org/img/wn/${weatherdata.icon}@2x.png`} alt="Weather Icon" />
                                        <p className="description">{weatherdata.description}</p>
                                        </>
                                        </div>
                                    )}
                                    {error && (
                                        
                                        <>
                                            <img src="error.png" className="error-img" alt="invalid location" />
                    
                                            <p className="error">{error}</p>
                                        </>
                                    )}
                                    {weatherdata.temperature && (
                                        <div className="weatherDataInfos">
                                        <>  <p className="now">Now</p>
                                            <p className="temperature">{weatherdata.temperature}°</p>
                                            <p className="dataTime">thermal sensation: {weatherdata.feelsLike}°</p>
                                        </>
                                        </div>
                                        
                                        
                                    )}
                                    {weatherdata.temperature && (
                                        <>  
                                        <hr className="separator"></hr> 
                                        </>
                                    )}

                                    {weatherdata.temperature && (
                                        <div className="weatherDataInfosFooter">
                                        <>  <p className="tempMax">maximum: {weatherdata.tempMax}°C</p>
                                            <p className="humidity">humidity: {weatherdata.humidity}%</p>
                                            <p className="tempMin">minimum: {weatherdata.tempMin}°C</p>
                                            <p className="cloudiness">speed: {weatherdata.speed}km/h</p>
                                        </>
                                        </div>
                                        
                                        
                                    )}
                                </>
                            )}
                        </div>
                    </form>
                </div>
            </div>
        </section>
    );
};

export default WeatherAppViews;