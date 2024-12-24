import React from 'react';
import '../css/LoadingSpinner.css';

const LoadingSpinner: React.FC = () => {
    return (
        <div className="loader">
            <div className="dot"></div>
            <div className="dot"></div>
            <div className="dot"></div>
        </div>
    );
};

export default LoadingSpinner;