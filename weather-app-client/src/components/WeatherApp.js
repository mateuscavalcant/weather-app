import React, { useState } from 'react';
import './WeatherApp.css'

const WeatherApp = () => {
    const [location, setLocation] = useState('');
    const [weatherdata, setWeatherData] = useState({ city: '', icon: '', description: '', temperature: '' });
    const [error, setError] = useState('');

    const handleSubmit = (event) => {
        event.preventDefault();

        fetch("http://localhost:8080/weather", {
            method: "POST",
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ location: location })
        })
            .then(response => response.json())
            .then(data => {
                console.log(data); // Debugging
                if (data.error) {
                    setError(data.error);
                    setWeatherData({ city: '', icon: '', description: '', temperature: '' });
                } else {
                    setError('');
                    setWeatherData(data.weatherdata || {}); // Garante que sempre é um objeto
                }
            })
            .catch(error => {
                console.error(error);
                setError('Error fetching weather data');
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
                        </div>
                    </form>
                </div>
            </div>
        </section>
    );
};

export default WeatherApp;
