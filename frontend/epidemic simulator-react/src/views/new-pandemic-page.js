import React, { useState } from 'react';
import { Helmet } from 'react-helmet';
import './new-pandemic-page.css';
import { useHistory } from 'react-router-dom';
import axios from '../api/axios';
import { toast } from 'react-toastify';

const CREATE_POPULATION_URL = '/sim/createPopulation';


const NewPandemicPage = (props) => {
 
  const [name, setName] = useState('');
  const [population, setPopulation] = useState(500000);
  const [initiallyInfected, setInitiallyInfected] = useState(100);
  const [minContacts, setMinContacts] = useState(5);
  const [maxContacts, setMaxContacts] = useState(20);
  const [minIncubationPeriod, setMinIncubationPeriod] = useState(7);
  const [maxIncubationPeriod, setMaxIncubationPeriod] = useState(14);
  const [minInfectiousPeriod, setMinInfectiousPeriod] = useState(5);
  const [maxInfectiousPeriod, setMaxInfectiousPeriod] = useState(15);
  const [transmissionProbability, setTransmissionProbability] = useState(50);

  const history = useHistory();

  const handleNextButton = async () => {

    const userId = localStorage.getItem('userId');

    const numPopulation = parseInt(population);
    const numInitiallyInfected = parseInt(initiallyInfected);
    const numMinContacts = parseInt(minContacts);
    const numMaxContacts = parseInt(maxContacts);
    const numMinIncubationPeriod = parseInt(minIncubationPeriod);
    const numMaxIncubationPeriod = parseInt(maxIncubationPeriod);
    const numMinInfectiousPeriod = parseInt(minInfectiousPeriod);
    const numMaxInfectiousPeriod = parseInt(maxInfectiousPeriod);
    const numTransmissionProbability = parseInt(transmissionProbability);


    if (!name.trim()) {
      toast.error('Name cannot be empty');
      return;
    }
    if (numInitiallyInfected >= numPopulation) {
      toast.error('Number of initially infected people must be less than population size');
      return;
    }
    if (numMinContacts >= numMaxContacts) {
      toast.error('Minimum number of contacts must be less than maximum number of contacts');
      console.log(numMinContacts);
      console.log(numMaxContacts);
      return;
    }
    if (numMinIncubationPeriod >= numMaxIncubationPeriod) {
      toast.error('Minimum incubation period must be less than maximum incubation period');
      return;
    }
    if (numMinInfectiousPeriod >= numMaxInfectiousPeriod) {
      toast.error('Minimum infectious period must be less than maximum infectious period');
      return;
    }


    const data = {
      userId: parseInt(userId),
      name: name,
      populationSize: population,
      initiallyInfected: initiallyInfected,
      minContacts: minContacts,
      maxContacts: maxContacts,
      minIncubationPeriod: minIncubationPeriod,
      maxIncubationPeriod: maxIncubationPeriod,
      minInfectiousPeriod: minInfectiousPeriod,
      maxInfectiousPeriod: maxInfectiousPeriod,
      transmissionProbability: transmissionProbability
    };

    

    try {

      const toastId = toast.loading('Initialize population');
      
      const response = await axios.post(CREATE_POPULATION_URL, data, {
        headers: { 'Content-Type': 'application/json' }
      });

      toast.update(toastId, { render: 'Population created successfully', type: 'success', isLoading: false, autoClose: 5000 });

      //toast.done('Population created successfully');

      console.log(JSON.stringify(response?.data));

      history.push('/simulation-page');
    } catch (err) {
      if (err.response?.status === 400) {
        toast.error(JSON.stringify(response?.data));
      }
      else {
        toast.error('Failed to create population');
      }
    }

  };

  return (
    <div className="new-pandemic-page-container">
      <Helmet>
        <title>NewPandemicPage - Epidemic Simulator</title>
        <meta
          property="og:title"
          content="Epidemic Simulator"
        />
      </Helmet>
      <div className="new-pandemic-page-container01">
        <h1 className="new-pandemic-page-text">Parameters</h1>
        <div className="new-pandemic-page-container02">
          <label className="new-pandemic-page-text01">Name</label>
          <input 
            type="text" 
            className="new-pandemic-page-textinput input"
            value={name}
            onChange={(e) => setName(e.target.value)}
          />
        </div>
        <div className="slider-container">
          <label>Population</label>
          <input
            type="range"
            className="new-pandemic-page-slider"
            min="1000"
            max="3000000"
            step="100"
            value={population}
            onChange={(e) => setPopulation(parseInt(e.target.value))}
          />
          <div>{population}</div>
        </div>
        <div className="slider-container">
          <label>Number of initially infected people</label>
          <input
            type="range"
            className="new-pandemic-page-slider"
            min="1"
            max="100"
            step="1"
            value={initiallyInfected}
            onChange={(e) => setInitiallyInfected(parseInt(e.target.value))}
          />
          <div>{initiallyInfected}</div>
        </div>
        <div className="slider-container">
          <label>Minimum number of contacts</label>
          <input
            type="range"
            className="new-pandemic-page-slider"
            min="0"
            max="5"
            step="1"
            value={minContacts}
            onChange={(e) => setMinContacts(parseInt(e.target.value))}
          />
          <div>{minContacts}</div>
        </div>
        <div className="slider-container">
          <label>Maximum number of contacts</label>
          <input
            type="range"
            className="new-pandemic-page-slider"
            min="0"
            max="20"
            step="1"
            value={maxContacts}
            onChange={(e) => setMaxContacts(parseInt(e.target.value))}
          />
          <div>{maxContacts}</div>
        </div>
        <div className="slider-container">
          <label>Minimum incubation period</label>
          <input
            type="range"
            className="new-pandemic-page-slider"
            min="1"
            max="7"
            step="1"
            value={minIncubationPeriod}
            onChange={(e) => setMinIncubationPeriod(parseInt(e.target.value))}
          />
          <div>{minIncubationPeriod} days</div>
        </div>
        <div className="slider-container">
          <label>Maximum incubation period</label>
          <input
            type="range"
            className="new-pandemic-page-slider"
            min="1"
            max="20"
            step="1"
            value={maxIncubationPeriod}
            onChange={(e) => setMaxIncubationPeriod(parseInt(e.target.value))}
          />
          <div>{maxIncubationPeriod} days</div>
        </div>
        <div className="slider-container">
          <label>Minimum infectious period</label>
          <input
            type="range"
            className="new-pandemic-page-slider"
            min="1"
            max="15"
            step="1"
            value={minInfectiousPeriod}
            onChange={(e) => setMinInfectiousPeriod(parseInt(e.target.value))}
          />
          <div>{minInfectiousPeriod} days</div>
        </div>
        <div className="slider-container">
          <label>Maximum infectious period</label>
          <input
            type="range"
            className="new-pandemic-page-slider"
            min="1"
            max="60"
            step="1"
            value={maxInfectiousPeriod}
            onChange={(e) => setMaxInfectiousPeriod(parseInt(e.target.value))}
          />
          <div>{maxInfectiousPeriod} days</div>
        </div>
        <div className="slider-container">
          <label>Probability of disease transmission</label>
          <input
            type="range"
            className="new-pandemic-page-slider"
            min="1"
            max="100"
            step="1"
            value={transmissionProbability}
            onChange={(e) => setTransmissionProbability(parseInt(e.target.value))}
          />
          <div>{transmissionProbability} %</div>
        </div>
        <button type="button" className="new-pandemic-page-button button" onClick={handleNextButton}>
          Next
        </button>
      </div>
    </div>
  );
};

export default NewPandemicPage;
