import React from "react";
import WeatherAppViews from "../components/WeatherAppViews";
import '../css/WeatherApp.css'

const WeatherApp: React.FC = () => {
  return (
    <div>
      <WeatherAppViews/>
    </div>
  );
};

export default WeatherApp;