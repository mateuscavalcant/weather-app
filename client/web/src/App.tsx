import React from "react";
import { BrowserRouter as Router, Routes, Route} from "react-router-dom";
import "./App.css";
import WeatherApp from "./pages/WeatherApp";


const App: React.FC = () => {
  return (
    <Router>
      <div className="App">
        <Routes>
          <Route path="/" element={<WeatherApp />} />
        </Routes>
      </div>
    </Router>
  );
};

export default App;