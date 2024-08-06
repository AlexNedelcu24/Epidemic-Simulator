import React, { useEffect, useState } from 'react'

import { Helmet } from 'react-helmet'
import { useHistory } from 'react-router-dom'
import './load-an-existing-pandemic-page.css'

import axios from '../api/axios';

const SIMULATIONS_URL = '/sim/usersSimulations';

const LoadAnExistingPandemicPage = (props) => {

  const history = useHistory()
  const [simulations, setSimulations] = useState([]);

  useEffect(() => {

    const userIdString = localStorage.getItem('userId');
    const id = parseInt(userIdString, 10);

    const fetchSimulations = async () => {
      try {
        const response = await axios.post(SIMULATIONS_URL,
          JSON.stringify({ id }),
          {
              headers: { 'Content-Type': 'application/json'}
          }
      );
        setSimulations(response.data);
      } catch (error) {
        console.error('Failed to fetch simulations:', error);
      }
    };

    fetchSimulations();
  }, []);



  const handleLoadSim = (simulationId) => {
    // logica 

    localStorage.setItem('simulationId', simulationId);
    history.push('/results-page');
  }

  return (
    <div className="load-an-existing-pandemic-page-container">
      <Helmet>
        <title>Epidemic Simulator</title>
        <meta
          property="og:title"
          content="Epidemic Simulator"
        />
      </Helmet>
      <div className="load-an-existing-pandemic-page-container1">
        <h1 className="load-an-existing-pandemic-page-text">
          Choose a simulation
        </h1>
        {simulations.map((simulation) => (
          <button
            key={simulation.id}
            type="button"
            className="load-an-existing-pandemic-page-button"
            onClick={() => handleLoadSim(simulation.id)}
          >
            {simulation.name}
          </button>
        ))}
      </div>
    </div>
  )
}

export default LoadAnExistingPandemicPage
