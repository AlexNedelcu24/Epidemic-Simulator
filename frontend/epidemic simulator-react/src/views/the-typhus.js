import React, { useEffect, useState } from 'react'

import { Helmet } from 'react-helmet'

import './the-typhus.css'

import axios from '../api/axios';

const PAGE_URL = '/page';

const TheTyphus = (props) => {

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
        const response = await axios.post(PAGE_URL, { id: 8 });
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
    <div className="the-typhus-container">
      <Helmet>
        <title>TheTyphus - Epidemic Simulator</title>
        <meta property="og:title" content="TheTyphus - Epidemic Simulator" />
      </Helmet>
      <div className="the-typhus-container1">
        <h1 className="the-typhus-text log">{pageData.title}</h1>
        <span className="the-typhus-text1">
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
          className="the-typhus-iframe"
        ></iframe>
      </div>
    </div>
  )
}

export default TheTyphus
