import React, { useEffect, useState } from 'react'

import { Helmet } from 'react-helmet'

import './the-smallpox.css'

import axios from '../api/axios';

const PAGE_URL = '/page';

const TheSmallpox = (props) => {

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
        const response = await axios.post(PAGE_URL, { id: 7 });
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
    <div className="the-smallpox-container">
      <Helmet>
        <title>TheSmallpox - Epidemic Simulator</title>
        <meta property="og:title" content="TheSmallpox - Epidemic Simulator" />
      </Helmet>
      <div className="the-smallpox-container1">
        <h1 className="the-smallpox-text log">{pageData.title}</h1>
        <span className="the-smallpox-text1">
            {pageData.text}
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
          className="the-smallpox-iframe"
        ></iframe>
      </div>
    </div>
  )
}

export default TheSmallpox
