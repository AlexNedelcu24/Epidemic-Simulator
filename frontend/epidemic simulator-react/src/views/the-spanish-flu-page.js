import React, { useEffect, useState } from 'react';

import { Helmet } from 'react-helmet'

import axios from '../api/axios';

import './the-spanish-flu-page.css'


const PAGE_URL = '/page';

const TheSpanishFluPage = (props) => {

  const [videoLoaded, setVideoLoaded] = useState(false);

  const handleVideoLoad = () => {
    setVideoLoaded(true);
  }

  const [pageData, setPageData] = useState({ title: '', text: '', link: '' });
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);


  useEffect(() => {
    const fetchData = async () => {
      try {
        const response = await axios.post(PAGE_URL, { id: 1 });
        const data = response.data;
        data.text = data.text.replace(/&quot;/g, '"').replace(/&apos;/g, "'");
        setPageData(response.data);
        setLoading(false);
      } catch (error) {
        setError('Failed to fetch data');
        setLoading(false);
      }
    };

    fetchData();
  }, []);

  if (loading) {
    return <div>Loading...</div>;
  }

  if (error) {
    return <div>{error}</div>;
  }







  return (
    <div className="the-spanish-flu-page-container">
      <Helmet>
        <title>Epidemic Simulator</title>
        <meta
          property="og:title"
          content="TheSpanishFluPage - Epidemic Simulator"
        />
      </Helmet>
      <div className="the-spanish-flu-page-container1">
        <div className="the-spanish-flu-page-container2">
          <h1 className="the-spanish-flu-page-text log">{pageData.title}</h1>
          <span className="the-spanish-flu-page-text1">
            <span>
                {pageData.text}
            </span>  
          </span>

          {!videoLoaded && (
            <img 
              className="the-spanish-flu-page-placeholder"
              onLoad={() => setVideoLoaded(true)}
            />
          )}

          <iframe
            src={pageData.link}
            onLoad={handleVideoLoad}
            style={{display: videoLoaded ? 'block' : 'none'}}
            allow="accelerometer, autoplay"
            allowFullScreen="true"
            className="the-spanish-flu-page-iframe"
          ></iframe>
        </div>
      </div>
    </div>
  )
}

export default TheSpanishFluPage
