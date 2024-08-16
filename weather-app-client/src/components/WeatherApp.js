import React, { useState } from 'react';
import './WeatherApp.css';
import LoadingSpinner from './LoadingSpinner'; 

const WeatherApp = () => {
    const [location, setLocation] = useState('');
    const [weatherdata, setWeatherData] = useState({ city: '', icon: '', description: '', temperature: '' });
    const [error, setError] = useState('');
    const [loading, setLoading] = useState(false);

    const handleSubmit = (event) => {
        event.preventDefault();
        setLoading(true); 

        fetch("http://localhost:8080/weather", {
            method: "POST",
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ location: location })
        })
            .then(response => {
                if (!response.ok) {
                    throw new Error('Network response was not ok');
                }
                return response.json();
            })
            .then(data => {
                console.log(data);
                if (data.error) {
                    setError(data.error);
                    setWeatherData({ city: '', icon: '', description: '', temperature: '' });
                } else {
                    setError('');
                    setWeatherData(data.weatherdata || {});
                }
                setLoading(false);
            })
            .catch(error => {
                console.error('Error:', error);
                setError('Error fetching weather data');
                setLoading(false);
            });
        
    };

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
                                        <img src={`https://openweathermap.org/img/wn/${weatherdata.icon}@2x.png`} alt="Weather Icon" />
                                    )}
                                    {error && (
                                        <>
                                            <img src="error.png" alt="invalid location" />
                                            <p className="error">{error}</p>
                                        </>
                                    )}
                                    {weatherdata.temperature && (
                                        <>
                                            <p className="temperature">{weatherdata.temperature}</p>
                                            <p className="description">{weatherdata.description}</p>
                                        </>
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

export default WeatherApp;
