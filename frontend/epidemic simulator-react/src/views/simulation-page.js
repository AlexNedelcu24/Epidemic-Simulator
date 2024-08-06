import React, { useEffect, useState } from 'react';
import { Helmet } from 'react-helmet';
import L from 'leaflet';
import './simulation-page.css';
import 'leaflet/dist/leaflet.css';
import {worldCt} from '../data/modified_countries.js';

import { useHistory } from 'react-router-dom'

import { toast } from 'react-toastify';
import axios from '../api/axios';


import '../maps/map.css';

import 'leaflet/dist/leaflet.js';

const START_URL = '/sim/startSimulation';
const PAUSE_URL  = '/sim/pauseSimulation';
const CONTINUE_URL = '/sim/continueSimulation';

const INTERVENTION_URL = '/sim/applyIntervention';

const GETPERCEN_URL = '/sim/infectionsPerCountry';

const STARTPARALLEL_URL = '/sim/startParallel';
const VERSTATUS_URL = '/sim/simulatingOver';


const SimulationPage = (props) => {

  const history = useHistory()

  const [simulationStarted, setSimulationStarted] = useState(false); 


  const [vaccinatedPeople, setVaccinatedPeople] = useState(50);
  const [socialDistancing, setSocialDistancing] = useState(10);


  const [selectedCountry, setSelectedCountry] = useState('');
  const [selectedCountryId, setSelectedCountryId] = useState(0);


  const [selectedAction, setSelectedAction] = useState('');
  const [his, setHis] = useState([]);

  const [status, setStatus] = useState('Status');

  const [pauseStatus, setPauseStatus] = useState('Pause');

  //const [casesVector, setCasesVector] = useState(new Array(255).fill(0));

  let casesVector = new Array(255).fill(0);


  const [simulationOver, setSimulationOver] = useState(false);


  const [simulationParallelStarted, setSimulationParallelStarted] = useState(false); 
  const [simulationParallelOver, setSimulationParallelOver] = useState(false); 



  const handleStart = async () => {
    // logica de pornire

    const userIdString = localStorage.getItem('userId');
    const id = parseInt(userIdString, 10);

    try {
      const response = await axios.post(START_URL,
          JSON.stringify({ id }),
          {
              headers: { 'Content-Type': 'application/json'}
          }
      );

      setStatus('Simulating');

      //const userId = JSON.stringify(response?.data)
      console.log("started");
      console.log(id);
    } catch (err) {
        if (!err?.response) {
            toast.error('No Server Response');
        } else if (err.response?.status === 400) {
          const errorMessage = err.response?.data?.message || 'Bad Request';
          toast.error(errorMessage);
        } else {
            toast.error('Bad request')
        }
        
    }
  }




  const handleStartParallel = async () => {
    // logica de pornire

    const userIdString = localStorage.getItem('userId');
    const id = parseInt(userIdString, 10);

    try {
      const response = await axios.post(STARTPARALLEL_URL,
          JSON.stringify({ id }),
          {
              headers: { 'Content-Type': 'application/json'}
          }
      );

      setStatus('Simulating');

      console.log("started parallel");
      console.log(id);
    } catch (err) {
        if (!err?.response) {
            toast.error('No Server Response');
        } else if (err.response?.status === 400) {
          const errorMessage = err.response?.data?.message || 'Bad Request';
          toast.error(errorMessage);
        } else {
            toast.error('Bad request')
        }
        
    }
  }

  useEffect(() => {
    const fetchData = async () => {
      const userIdString = localStorage.getItem('userId');
      const id = parseInt(userIdString, 10);
  
      console.log(id);
  
      try {
        const response = await axios.post(VERSTATUS_URL, JSON.stringify({ id }), {
          headers: { 'Content-Type': 'application/json' },
        });
  
        if (response.data === 'Simulation is over') {
          setStatus('Simulation finished');
          setSimulationParallelOver(true);
          return;
        }
  
      } catch (err) {
        console.error('Failed to fetch data:', err);
      }
    };
  
    if (simulationParallelStarted && !simulationParallelOver) {
      const intervalId = setInterval(fetchData, 1000);
      return () => clearInterval(intervalId);
    }
  }, [simulationParallelStarted, simulationParallelOver]);
  















  const handlePause = async () => {
    // logica de pauza

    const userIdString = localStorage.getItem('userId');
    const id = parseInt(userIdString, 10);

    if (pauseStatus === 'Pause') {
      
      try {
      const response = await axios.post(PAUSE_URL,
          JSON.stringify({ id }),
          {
              headers: { 'Content-Type': 'application/json'}
          }
      );

      setStatus('Paused');
      setPauseStatus('Continue');

      console.log("paused");
      console.log(id);
    } catch (err) {
        if (!err?.response) {
            toast.error('No Server Response');
        } else if (err.response?.status === 400) {
          const errorMessage = err.response?.data?.message || 'Bad Request';
          toast.error(errorMessage);
        } else {
            toast.error('Bad request')
        }
        
    }
    
    } else if(pauseStatus === 'Continue') {

      try {
        const response = await axios.post(CONTINUE_URL,
            JSON.stringify({ id }),
            {
                headers: { 'Content-Type': 'application/json'}
            }
        );
  
        setStatus('Simulating');
        setPauseStatus('Pause');
  
        console.log("paused");
        console.log(id);
      } catch (err) {
          if (!err?.response) {
              toast.error('No Server Response');
          } else if (err.response?.status === 400) {
            const errorMessage = err.response?.data?.message || 'Bad Request';
            toast.error(errorMessage);
          } else {
              toast.error('Bad request')
          }
          
      }

    }
  
  }


  const handleExit = () => {
    history.push('/main-page')
  }


  useEffect(() => {
    // Function to fetch data from the API
    const fetchData = async () => {
      const userIdString = localStorage.getItem('userId');
      const id = parseInt(userIdString, 10);
      
      console.log(id);

      try {
        const response = await axios.post(GETPERCEN_URL,
            JSON.stringify({ id }),
            {
                headers: { 'Content-Type': 'application/json'}
            }
        );

        if (response.data === "Simulation is over") {
          setSimulationOver(true);
          setStatus('Simulation finished');
          return;
        }

        //console.log("heeeey");

        response.data.forEach((value, index) => {
          casesVector[index] = parseFloat(parseFloat(value).toFixed(2));
          //casesVector[index] = parseInt(value, 10);
        });


      } catch (err) {
        console.error('Failed to fetch data:', err);
      }
    };

    // Fetch data every 2 seconds
    if (simulationStarted && !simulationOver) {
      const intervalId = setInterval(fetchData, 1000);
      return () => clearInterval(intervalId);
    }
  }, [simulationStarted]);









  useEffect(() => {
    // Map initialization code adapted from map.js
    const baseLayer = L.tileLayer('https://tile.openstreetmap.org/{z}/{x}/{y}.png', {
      maxZoom: 8,
      minZoom: 3,
      attribution: '&copy; <a href="http://www.openstreetmap.org/copyright">OpenStreetMap</a>',
      noWrap: true
    });

    // Initialize the map
    const map = L.map('map', {
      center: [22.735, 79.892],
      zoom: 5,
      zoomControl: false,
      scrollWheelZoom: true,
      layers: [baseLayer],
      maxBounds: [[-90, -180], [90, 180]],
      maxBoundsViscosity: 1.0
    });


    

    
    /*let casesVector = [];
    for (let i = 1; i <= 255; i++) {
        casesVector.push(i);
    }*/

    var info = L.control();

    info.onAdd = function (map) {
        this._div = L.DomUtil.create('div', 'info');
        this.update();
        return this._div;
    };

    info.update = function (props, id) {
        let nrCases = casesVector[id-1];

        this._div.innerHTML = '<h4>Percentage of infected people</h4>' +  (props ?
            '<br /><b>' + props.ADMIN + '</b><br />' + (nrCases).toFixed(2) + '%' : '<br />Hover over a state');
    };

    info.addTo(map);

    var countryInfo = L.control({position: 'topleft'});

    countryInfo.onAdd = function (map) {
        this._div = L.DomUtil.create('div', 'country-info');
        this.update();
        return this._div;
    };

    countryInfo.update = function (countryName) {
        this._div.innerHTML = '<h4>Selected Country</h4>' +  (countryName ? '<b>' + countryName + '</b>' : 'Click on a country');
    };

    countryInfo.addTo(map);






    function getColor(d) {
        return d > 75 ? '#800026' :
              d > 65  ? '#BD0026' :
              d > 50  ? '#E31A1C' :
              d > 35  ? '#FC4E2A' :
              d > 15   ? '#FD8D3C' :
              d > 5   ? '#FEB24C' :
              d > 0.5   ? '#FED976' :
                          '#FFEDA0';
    }

    function style(feature) {
      let featureCases = simulationStarted ? casesVector[feature.id - 1] : 0;

      return {
        weight: 1,
        opacity: 1,
        color: 'red',
        dashArray: '',
        fillOpacity: simulationStarted ? 0.7 : 0, 
        fillColor: getColor(featureCases)
      };
    }

    function highlightFeature(e) {
        var layer = e.target;

        layer.setStyle({
            weight: 5,
            color: '#666',
            dashArray: '',
            fillOpacity: 0.7
        });

        if(!L.Browser.ie && !L.Browser.opera && !L.Browser.edge){
            layer.bringToFront();
        }

        info.update(layer.feature.properties,layer.feature.id);
        
    }

    function resetHighlight(e) {
        geojson.resetStyle(e.target);
        info.update();
    }

    function zoomToFeature(e) {
        map.fitBounds(e.target.getBounds());
    }

    function onEachFeature(feature, layer) {
        layer.on({
            mouseover: highlightFeature,
            mouseout: resetHighlight,
            click: function(e) {
              zoomToFeature(e);
              const countryName = feature.properties.ADMIN;
              const countryId = feature.id; 
              setSelectedCountry(countryName);
              setSelectedCountryId(countryId);
              countryInfo.update(countryName); 
            }
        });
    }

    var geojson = L.geoJson(worldCt, {
        style: style,
        onEachFeature: onEachFeature
    }).addTo(map);



    /*function updateCasesVectorAndMap() {
      if (simulationStarted) {
        for (let i = 0; i < casesVector.length; i++) {
          casesVector[i] += 2;
        }

        geojson.eachLayer(function (layer) {
          layer.setStyle(style(layer.feature));
        });
      }
    }

    const intervalId = setInterval(updateCasesVectorAndMap, 2000);*/


    const intervalId = setInterval(() => {
      geojson.eachLayer(function (layer) {
        layer.setStyle(style(layer.feature));
      });
    }, 2000);


    var legend = L.control({position: 'bottomright'});

    legend.onAdd = function (map) {

        var div = L.DomUtil.create('div', 'info legend'),
            grades = [0, 0.5, 5, 15, 35, 50, 65, 75],
            labels = [],
            from, to;

        for (var i = 0; i < grades.length; i++) {
            from = grades[i];
            to = grades[i+1];

            labels.push(
                '<i style="background:' + getColor(from + 1) + '"></i> ' +
                (from) + (to ? ' &ndash; ' + (to) + ' %' : '+ %')
            );
        }

        div.innerHTML = labels.join('<br>');

        return div;
    };

    legend.addTo(map);

    return () => {
      clearInterval(intervalId);
      map.remove();
    };
  }, [simulationStarted]);


  const addToHistory = async () => {
    if (selectedCountry && selectedAction) {

      const userIdString = localStorage.getItem('userId');
      const userId = parseInt(userIdString, 10);
      const name = selectedAction;
      const country = selectedCountryId
      const countryName = selectedCountry;
      
      // Determină valoarea și unitatea pe baza acțiunii selectate
      const value = selectedAction === 'Vaccination' ? vaccinatedPeople : socialDistancing;
      const unit = selectedAction === 'Vaccination' ? '%' : 'days';

      try {
        const response = await axios.post(INTERVENTION_URL, 
          JSON.stringify({ userId, name, country, countryName, value}), 
          {
              headers: { 'Content-Type': 'application/json' },
          }
        );

        if (response.data === 'Intervention applied!') {
          console.log("am aplicaaaaaaat");
          
          // Construiește șirul de caractere pentru elementul de istoric
          const newHistoryItem = `${selectedCountry} - ${selectedAction} - ${value} ${unit}`;
      
          // Adaugă noul element în istoric
          setHis([...his, newHistoryItem]);
        } else {
          toast.error('Failed to apply intervention');
        }
      } catch (err) {
        console.error('Failed to apply intervention:', err);
        toast.error('Error applying intervention');
      }

  
      // Construiește șirul de caractere pentru elementul de istoric
      const newHistoryItem = `${selectedCountry} - ${selectedAction} - ${value} ${unit}`;
      
      // Adaugă noul element în istoric
      setHis([...his, newHistoryItem]);
    } else {
      // Afișează o alertă dacă țara sau acțiunea nu au fost selectate
      toast.error('Please select a country and an action first.');
    }
  };
  



  return (
    <div className="simulation-page-container">
      <Helmet>
        <title>Epidemic Simulator</title>
        <meta
          property="og:title"
          content="SimulationPage - Epidemic Simulator"
        />
      </Helmet>
      <div className="simulation-page-container01">
        <div className="simulation-page-container02">
          <div className="simulation-page-container03">
            <label className="log">PANDEMIC SIMULATOR</label>
          </div>
        </div>
        <div className="simulation-page-container04">
          <button type="button" className="simulation-page-button button" onClick={() => {
            setSimulationStarted(true);
            handleStart(); 
            } }>
            Start
          </button>
          <button type="button" className="simulation-page-button10 button" onClick={() => {
            setSimulationParallelStarted(true);
            handleStartParallel();
            } }>
            Get results faster
          </button>
          <button type="button" className="simulation-page-button1 button" onClick={() => handlePause()}>
            {pauseStatus}
          </button>
          <label className="simulation-page-text1 lbl">{status}</label>
        </div>
      </div>

      <div id="map"></div>

      <div className="simulation-page-container05">
        <div className="simulation-page-container06">
          <label className="simulation-page-text2 log">Interventions</label>
        </div>
        <div className="simulation-page-container07">
          <div className="simulation-page-container08">
            <div className="simulation-page-container09">
              <input
                type="radio"
                name="radio"
                value="Vaccination"
                className="simulation-page-radiobutton"
                onChange={(e) => setSelectedAction(e.target.value)}
              />
              <label className="simulation-page-text3 lbl">Vaccination</label>
              <div className="slider-container">
                <input
                  type="range"
                  className="new-pandemic-page-slider"
                  min="1"
                  max="100"
                  step="1"
                  value={vaccinatedPeople}
                  onChange={(e) => setVaccinatedPeople(e.target.value)}
                />
                <div>{vaccinatedPeople} %</div>
              </div>
            </div>
            <div className="simulation-page-container10">
              <input
                type="radio"
                name="radio"
                value="Social Distancing"
                className="simulation-page-radiobutton1"
                onChange={(e) => setSelectedAction(e.target.value)}
              />
              <label className="simulation-page-text4 lbl">
                Social Distancing
              </label>
              <div className="slider-container">
                <input
                  type="range"
                  className="new-pandemic-page-slider"
                  min="1"
                  max="20"
                  step="1"
                  value={socialDistancing}
                  onChange={(e) => setSocialDistancing(e.target.value)}
                />
                <div>{socialDistancing} days</div>
              </div>
            </div>
          </div>
          <div className="simulation-page-container11">
            <button type="button" className="simulation-page-button2 button" onClick={addToHistory}>
              Apply
            </button>
          </div>
        </div>
        <div className="simulation-page-container12">
          <h1 className="simulation-page-text5 lbl">
            <span>History:</span>
            <br></br>
          </h1>
          <ul className="simulation-page-ul list">
          {his.map((item, index) => (
              <li key={index}>{item}</li>
          ))}
          </ul>
        </div>
        <div className="simulation-page-container13">
          
          <button type="button" className="simulation-page-button4 button" onClick={handleExit}>
            Exit Simulation
          </button>
        </div>
      </div>
    </div>
  )

}

export default SimulationPage
