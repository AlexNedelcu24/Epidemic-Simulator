import React, { useEffect, useState } from 'react'

import { Helmet } from 'react-helmet'

import axios from '../api/axios';

import { Line } from 'react-chartjs-2';
import 'chart.js/auto';
import './results-page.css';

import './results-page.css'

const SIM_URL = '/sim/simulationDetails';
const INTER_URL = '/interventions';
const RES_URL = '/results';


const ResultsPage = (props) => {
  const [simulationDetails, setSimulationDetails] = useState(null);
  const [interventions, setInterventions] = useState([]);
  const [results, setResults] = useState([]);
  const [loadingResults, setLoadingResults] = useState(true);
  const [loading, setLoading] = useState(true);
  const [loadingInterventions, setLoadingInterventions] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    const fetchSimulationDetails = async () => {
      const simulationIdString = localStorage.getItem('simulationId');
      const id = parseInt(simulationIdString, 10);

      if (!id) {
        setError('No simulation ID found');
        setLoading(false);
        return;
      }

      try {
        const response = await axios.post(SIM_URL,
          JSON.stringify({ id }),
          {
              headers: { 'Content-Type': 'application/json'}
          }
        );

        setSimulationDetails(response.data);
      } catch (err) {
        console.error('Failed to fetch simulation details:', err);
        setError('Failed to fetch simulation details');
      } finally {
        setLoading(false);
      }
    };

    fetchSimulationDetails();
  }, []);





  useEffect(() => {
    const fetchInterventions = async () => {
      const simulationIdString = localStorage.getItem('simulationId');
      const id = parseInt(simulationIdString, 10);

      if (!id) {
        setError('No simulation ID found');
        setLoadingInterventions(false);
        return;
      }

      try {
        const response = await axios.post(INTER_URL,
          JSON.stringify({ id }),
          {
              headers: { 'Content-Type': 'application/json' }
          }
        );

        setInterventions(response.data);
      } catch (err) {
        console.error('Failed to fetch interventions:', err);
        setError('Failed to fetch interventions');
      } finally {
        setLoadingInterventions(false);
      }
    };

    fetchInterventions();
  }, []);




  useEffect(() => {
    const fetchResults = async () => {
      const simulationIdString = localStorage.getItem('simulationId');
      const id = parseInt(simulationIdString, 10);

      if (!id) {
        setError('No simulation ID found');
        setLoadingResults(false);
        return;
      }

      try {
        const response = await axios.post(RES_URL,
          JSON.stringify({ id }),
          {
              headers: { 'Content-Type': 'application/json' }
          }
        );

        setResults(response.data);
      } catch (err) {
        console.error('Failed to fetch results:', err);
        setError('Failed to fetch results');
      } finally {
        setLoadingResults(false);
      }
    };

    fetchResults();
  }, []);














  if (loading || loadingInterventions || loadingResults) {
    return <div>Loading...</div>;
  }

  if (error) {
    return <div>{error}</div>;
  }




  const chartData = {
    labels: results.map(result => result.day),
    datasets: [
      {
        label: 'Infections Number',
        data: results.map(result => result.infectionsNumber),
        fill: false,
        borderColor: 'rgba(255, 102, 102, 1)',
      },
    ],
  };


  const chartOptions = {
    responsive: true,
    maintainAspectRatio: false,
    scales: {
      x: {
        ticks: {
          color: 'black', // Culoarea textului pentru etichetele axei X
        }
      },
      y: {
        ticks: {
          color: 'black', // Culoarea textului pentru etichetele axei Y
        }
      }
    },
    plugins: {
      legend: {
        labels: {
          color: 'black', // Culoarea textului pentru legenda
        }
      }
    }
  };





  return (
    <div className="results-page-container">
      <Helmet>
        <title>ResultsPage - Epidemic Simulator</title>
        <meta property="og:title" content="Epidemic Simulator" />
      </Helmet>
      <div className="results-page-container1">
        <h1 className="results-page-text">{simulationDetails.name}</h1>
        <h1 className="results-page-text1">Parameters</h1>
        <ul className="list">
          <li className="results-page-li list-item">
            <span>Transmission Probability: {simulationDetails.transmissionProbability}</span>
          </li>
          <li className="results-page-li list-item">
            <span>Population Size: {simulationDetails.populationSize}</span>
          </li>
          <li className="results-page-li list-item">
            <span>Initially Infected: {simulationDetails.initiallyInfected}</span>
          </li>
          <li className="results-page-li list-item">
            <span>Min Contacts: {simulationDetails.minContacts}</span>
          </li>
          <li className="results-page-li list-item">
            <span>Max Contacts: {simulationDetails.maxContacts}</span>
          </li>
          <li className="results-page-li list-item">
            <span>Min Incubation Period: {simulationDetails.minIncubationPeriod}</span>
          </li>
          <li className="results-page-li list-item">
            <span>Max Incubation Period: {simulationDetails.maxIncubationPeriod}</span>
          </li>
          <li className="results-page-li list-item">
            <span>Min Infectious Period: {simulationDetails.minInfectiousPeriod}</span>
          </li>
          <li className="results-page-li list-item">
            <span>Max Infectious Period: {simulationDetails.maxInfectiousPeriod}</span>
          </li>
        </ul>
        <h1 className="results-page-text3">
          <span>Interventions</span>
          <br></br>
        </h1>
        <ul className="list">
          {interventions.map((intervention, index) => (
            <li key={index} className="results-page-li list-item">
              <span>{intervention.countryName} - {intervention.name}: {intervention.value} {intervention.name === "Vaccination" ? "%" : "days"}</span>
            </li>
          ))}
        </ul>
        <h1 className="results-page-text3">
          <span>Results</span>
          <br></br>
        </h1>
        <div className="results-chart">
          <Line data={chartData} options={chartOptions}/>
        </div>
      </div>
    </div>
  )
}

export default ResultsPage
