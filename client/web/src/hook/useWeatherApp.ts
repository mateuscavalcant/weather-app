import React, { useState } from 'react';

export const useWeatherApp = () => {
    const [location, setLocation] = useState('');
    const [weatherdata, setWeatherData] = useState({ 
        city: '', icon: '', description: '', temperature: '',
        humidity:'', tempMax:'', tempMin:'', speed: '', feelsLike:''
    });
    const [error, setError] = useState('');
    const [loading, setLoading] = useState(false);

    const handleSubmit = async (e: React.FormEvent): Promise<void> => {
        e.preventDefault();

        setLoading(true);

        fetch("http://localhost:8080/weather", {
            method: "POST",
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ location: location })
        })
            .then(response => response.json())
            .then(data => {
                if (data.weatherDataError) {
                    setError(data.weatherDataError);
                    setWeatherData({
                        city: '', icon: '', description: '', temperature: '',
                        humidity:'', tempMax:'', tempMin:'', speed: '', feelsLike:''
                    });
                } else {
                    setError('');
                    setWeatherData(data.weatherdata || {});
                }
                setLoading(false);
            })
            .catch(error => {
                console.error(error);
                setError('Error fetching weather data');
                setLoading(false);
            });
    };


    return {
        location,
        setLocation,
        weatherdata,
        setWeatherData,
        error,
        setError, 
        loading, 
        handleSubmit
    }

};

