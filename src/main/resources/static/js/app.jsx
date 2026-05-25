const { useState, useEffect } = React;

// ===== COMPONENTE CLASSIFICA =====
function Classifica({ torneoId }) {
    const [classifica, setClassifica] = useState([]);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        if (!torneoId) return;
        setLoading(true);
        fetch(`/api/tornei/${torneoId}/classifica`)
            .then(r => r.json())
            .then(data => { setClassifica(data); setLoading(false); });
    }, [torneoId]);

    if (loading) return <p style={{ color: 'var(--color-text)', letterSpacing: '1px' }}>Caricamento classifica...</p>;

    return (
        <div>
            <h2>🏆 Classifica</h2>
            <div style={{
                background: 'linear-gradient(135deg, var(--bg-card) 0%, #0a1628 100%)',
                border: '1px solid var(--border-color)',
                borderRadius: '4px',
                overflow: 'hidden'
            }}>
                <table style={{ width: '100%', borderCollapse: 'collapse' }}>
                    <thead>
                        <tr style={{
                            background: 'linear-gradient(90deg, rgba(200,155,60,0.15) 0%, rgba(200,155,60,0.05) 100%)',
                            borderBottom: '1px solid rgba(200,155,60,0.4)'
                        }}>
                            {['#', 'Squadra', 'P', 'V', 'N', 'S', 'GF', 'GS', 'DR', 'Pt'].map((h, i) => (
                                <th key={i} style={{
                                    padding: '0.7rem 0.5rem',
                                    textAlign: i <= 1 ? 'left' : 'center',
                                    color: 'var(--color-gold)',
                                    fontSize: '0.75rem',
                                    letterSpacing: '1.5px',
                                    fontWeight: 'normal',
                                    textTransform: 'uppercase'
                                }}>{h}</th>
                            ))}
                        </tr>
                    </thead>
                    <tbody>
                        {classifica.map((s, i) => (
                            <tr key={s.id} style={{
                                borderBottom: '1px solid rgba(30,58,95,0.6)',
                                background: i === 0
                                    ? 'rgba(200,155,60,0.08)'
                                    : i < 3
                                        ? 'rgba(30,90,160,0.05)'
                                        : 'transparent',
                                transition: 'background 0.2s'
                            }}
                                onMouseEnter={e => e.currentTarget.style.background = 'rgba(30,90,160,0.1)'}
                                onMouseLeave={e => e.currentTarget.style.background = i === 0 ? 'rgba(200,155,60,0.08)' : i < 3 ? 'rgba(30,90,160,0.05)' : 'transparent'}
                            >
                                <td style={{ padding: '0.6rem 0.5rem', color: 'var(--color-text)', fontSize: '0.85rem' }}>
                                    {i === 0 ? '🥇' : i === 1 ? '🥈' : i === 2 ? '🥉' : i + 1}
                                </td>
                                <td style={{
                                    padding: '0.6rem 0.5rem',
                                    color: i < 3 ? 'var(--color-gold-light)' : 'var(--color-text-light)',
                                    fontWeight: i < 3 ? 'bold' : 'normal',
                                    letterSpacing: '0.5px'
                                }}>{s.nome}</td>
                                <td style={{ padding: '0.6rem 0.5rem', textAlign: 'center', color: 'var(--color-text)', fontSize: '0.9rem' }}>{s.vinte + s.pareggiate + s.perse}</td>
                                <td style={{ padding: '0.6rem 0.5rem', textAlign: 'center', color: 'var(--ionia)', fontWeight: 'bold' }}>{s.vinte}</td>
                                <td style={{ padding: '0.6rem 0.5rem', textAlign: 'center', color: 'var(--color-text)' }}>{s.pareggiate}</td>
                                <td style={{ padding: '0.6rem 0.5rem', textAlign: 'center', color: 'var(--noxus)', fontWeight: 'bold' }}>{s.perse}</td>
                                <td style={{ padding: '0.6rem 0.5rem', textAlign: 'center', color: 'var(--color-text)' }}>{s.golfatti}</td>
                                <td style={{ padding: '0.6rem 0.5rem', textAlign: 'center', color: 'var(--color-text)' }}>{s.golsubiti}</td>
                                <td style={{ padding: '0.6rem 0.5rem', textAlign: 'center', color: s.differenzaReti > 0 ? 'var(--ionia)' : s.differenzaReti < 0 ? 'var(--noxus)' : 'var(--color-text)' }}>
                                    {s.differenzaReti > 0 ? '+' + s.differenzaReti : s.differenzaReti}
                                </td>
                                <td style={{ padding: '0.6rem 0.5rem', textAlign: 'center', color: 'var(--color-gold)', fontWeight: 'bold', fontSize: '1rem' }}>{s.punti}</td>
                            </tr>
                        ))}
                    </tbody>
                </table>
            </div>
        </div>
    );
}

// ===== COMPONENTE CALENDARIO =====
function Calendario({ torneoId }) {
    const [partite, setPartite] = useState([]);
    const [loading, setLoading] = useState(true);
    const [filtroStato, setFiltroStato] = useState('TUTTE');
    const [filtroSquadra, setFiltroSquadra] = useState('TUTTE');

    useEffect(() => {
        if (!torneoId) return;
        setLoading(true);
        fetch(`/api/tornei/${torneoId}/calendario`)
            .then(r => r.json())
            .then(data => { setPartite(data); setLoading(false); });
    }, [torneoId]);

    const squadre = [...new Set(
        partite.flatMap(p => [p.squadraCasa, p.squadraOspite])
    )].sort();

    const getRisultato = (p) => {
        if (filtroSquadra === 'TUTTE' || p.stato !== 'COMPLETATA') return null;
        const isCasa = p.squadraCasa === filtroSquadra;
        const gF = isCasa ? p.goalHome : p.goalGuest;
        const gS = isCasa ? p.goalGuest : p.goalHome;
        if (gF > gS) return { testo: 'V', colore: 'var(--ionia)' };
        if (gF === gS) return { testo: 'N', colore: 'var(--color-gold)' };
        return { testo: 'S', colore: 'var(--noxus)' };
    };

    const partiteFiltrate = partite
        .filter(p => filtroStato === 'TUTTE' || p.stato === filtroStato)
        .filter(p => filtroSquadra === 'TUTTE' ||
            p.squadraCasa === filtroSquadra ||
            p.squadraOspite === filtroSquadra);

    const formatData = (dataOra) => {
        const d = new Date(dataOra);
        return d.toLocaleDateString('it-IT', {
            day: '2-digit', month: '2-digit', year: 'numeric',
            hour: '2-digit', minute: '2-digit'
        });
    };

    const btnStyle = (attivo) => ({
        padding: '0.4rem 1rem',
        border: `1px solid ${attivo ? 'var(--color-gold)' : 'rgba(200,155,60,0.3)'}`,
        background: attivo ? 'rgba(200,155,60,0.15)' : 'transparent',
        color: attivo ? 'var(--color-gold)' : 'var(--color-text)',
        cursor: 'pointer',
        borderRadius: '2px',
        fontSize: '0.75rem',
        letterSpacing: '1px',
        textTransform: 'uppercase',
        transition: 'all 0.2s',
        boxShadow: attivo ? '0 0 10px rgba(200,155,60,0.2)' : 'none'
    });

    const selectStyle = {
        background: 'rgba(1,10,19,0.8)',
        border: '1px solid rgba(200,155,60,0.4)',
        color: 'var(--color-gold-light)',
        padding: '0.4rem 0.8rem',
        borderRadius: '2px',
        fontSize: '0.8rem',
        cursor: 'pointer',
        minWidth: '200px',
        letterSpacing: '0.5px'
    };

    if (loading) return <p style={{ color: 'var(--color-text)', letterSpacing: '1px' }}>Caricamento calendario...</p>;

    return (
        <div>
            <h2>📅 Calendario</h2>

            {/* Filtri */}
            <div style={{
                display: 'flex', gap: '1rem', flexWrap: 'wrap',
                alignItems: 'center', marginBottom: '1.5rem',
                padding: '1rem',
                background: 'linear-gradient(135deg, var(--bg-card) 0%, #0a1628 100%)',
                border: '1px solid var(--border-color)',
                borderRadius: '4px'
            }}>
                <div style={{ display: 'flex', gap: '0.5rem', flexWrap: 'wrap' }}>
                    {['TUTTE', 'COMPLETATA', 'IN_ATTESA'].map(f => (
                        <button key={f} onClick={() => setFiltroStato(f)} style={btnStyle(filtroStato === f)}>
                            {f === 'TUTTE' ? 'Tutte' : f === 'COMPLETATA' ? 'Completate' : 'In Attesa'}
                        </button>
                    ))}
                </div>
                <select value={filtroSquadra} onChange={e => setFiltroSquadra(e.target.value)} style={selectStyle}>
                    <option value="TUTTE">⚔ Tutte le squadre</option>
                    {squadre.map(s => (
                        <option key={s} value={s}>{s}</option>
                    ))}
                </select>
            </div>

            {/* Partite */}
            {partiteFiltrate.map(p => {
                const risultato = getRisultato(p);
                const isCompletata = p.stato === 'COMPLETATA';
                return (
                    <div key={p.id} style={{
                        background: 'linear-gradient(135deg, var(--bg-card) 0%, #0a1628 100%)',
                        border: '1px solid var(--border-color)',
                        borderRadius: '4px',
                        padding: '0.8rem 1rem',
                        marginBottom: '0.5rem',
                        display: 'flex',
                        justifyContent: 'space-between',
                        alignItems: 'center',
                        gap: '0.5rem',
                        transition: 'border-color 0.2s, box-shadow 0.2s'
                    }}
                        onMouseEnter={e => {
                            e.currentTarget.style.borderColor = 'rgba(200,155,60,0.4)';
                            e.currentTarget.style.boxShadow = '0 0 15px rgba(30,90,160,0.15)';
                        }}
                        onMouseLeave={e => {
                            e.currentTarget.style.borderColor = 'var(--border-color)';
                            e.currentTarget.style.boxShadow = 'none';
                        }}
                    >
                        {/* Badge V/N/S */}
                        {risultato && (
                            <div style={{
                                fontWeight: 'bold', fontSize: '0.85rem',
                                color: risultato.colore,
                                minWidth: '20px', textAlign: 'center',
                                letterSpacing: '1px'
                            }}>
                                {risultato.testo}
                            </div>
                        )}

                        {/* Squadra casa */}
                        <div style={{
                            flex: 1, textAlign: 'right',
                            color: filtroSquadra === p.squadraCasa ? 'var(--color-gold)' : 'var(--color-text-light)',
                            fontWeight: filtroSquadra === p.squadraCasa ? 'bold' : 'normal',
                            fontSize: 'clamp(0.75rem, 2vw, 0.9rem)',
                            letterSpacing: '0.5px'
                        }}>
                            {p.squadraCasa}
                        </div>

                        {/* Centro */}
                        <div style={{ padding: '0 0.8rem', textAlign: 'center', minWidth: '110px' }}>
                            {isCompletata
                                ? <span style={{
                                    color: 'var(--color-gold)',
                                    fontWeight: 'bold',
                                    fontSize: '1.1rem',
                                    textShadow: '0 0 10px rgba(200,155,60,0.3)'
                                }}>
                                    {p.goalHome} — {p.goalGuest}
                                </span>
                                : <span style={{
                                    color: 'var(--color-text)',
                                    fontSize: '0.75rem',
                                    letterSpacing: '0.5px'
                                }}>
                                    {formatData(p.dataOra)}
                                </span>
                            }
                            <div style={{
                                fontSize: '0.65rem',
                                color: 'var(--color-text)',
                                marginTop: '0.2rem',
                                letterSpacing: '0.5px',
                                opacity: 0.7
                            }}>
                                {p.luogo}
                            </div>
                        </div>

                        {/* Squadra ospite */}
                        <div style={{
                            flex: 1, textAlign: 'left',
                            color: filtroSquadra === p.squadraOspite ? 'var(--color-gold)' : 'var(--color-text-light)',
                            fontWeight: filtroSquadra === p.squadraOspite ? 'bold' : 'normal',
                            fontSize: 'clamp(0.75rem, 2vw, 0.9rem)',
                            letterSpacing: '0.5px'
                        }}>
                            {p.squadraOspite}
                        </div>
                    </div>
                );
            })}

            {partiteFiltrate.length === 0 &&
                <p style={{ color: 'var(--color-text)', letterSpacing: '1px' }}>Nessuna partita trovata.</p>
            }
        </div>
    );
}

// ===== APP PRINCIPALE =====
function App() {
    const [tornei, setTornei] = useState([]);
    const [torneoSelezionato, setTorneoSelezionato] = useState(null);
    const [vista, setVista] = useState('classifica');

    useEffect(() => {
        fetch('/api/tornei')
            .then(r => r.json())
            .then(data => {
                setTornei(data);
                if (data.length > 0) setTorneoSelezionato(data[0].id);
            });
    }, []);

    return (
        <div>
            {/* Selettore torneo */}
            <div style={{
                marginBottom: '1.5rem',
                display: 'flex', gap: '1rem',
                alignItems: 'center', flexWrap: 'wrap'
            }}>
                <span style={{
                    color: 'var(--color-text)',
                    fontSize: '0.75rem',
                    letterSpacing: '2px',
                    textTransform: 'uppercase'
                }}>Torneo:</span>
                {tornei.map(t => (
                    <button key={t.id} onClick={() => setTorneoSelezionato(t.id)}
                        style={{
                            padding: '0.4rem 1.2rem',
                            border: `1px solid ${torneoSelezionato === t.id ? 'var(--color-gold)' : 'rgba(200,155,60,0.3)'}`,
                            background: torneoSelezionato === t.id ? 'rgba(200,155,60,0.15)' : 'transparent',
                            color: torneoSelezionato === t.id ? 'var(--color-gold)' : 'var(--color-text)',
                            cursor: 'pointer', borderRadius: '2px',
                            fontSize: '0.8rem', letterSpacing: '1px',
                            textTransform: 'uppercase',
                            boxShadow: torneoSelezionato === t.id ? '0 0 12px rgba(200,155,60,0.2)' : 'none',
                            transition: 'all 0.2s'
                        }}>
                        {t.nome}
                    </button>
                ))}
            </div>

            {/* Selettore vista */}
            <div style={{ marginBottom: '1.5rem', display: 'flex', gap: '0.5rem' }}>
                {['classifica', 'calendario'].map(v => (
                    <button key={v} onClick={() => setVista(v)}
                        style={{
                            padding: '0.5rem 1.5rem',
                            border: `1px solid ${vista === v ? 'rgba(200,155,60,0.5)' : 'var(--border-color)'}`,
                            background: vista === v
                                ? 'linear-gradient(135deg, var(--bg-card) 0%, #0a1628 100%)'
                                : 'transparent',
                            color: vista === v ? 'var(--color-gold)' : 'var(--color-text)',
                            cursor: 'pointer', borderRadius: '2px',
                            textTransform: 'uppercase', letterSpacing: '1.5px',
                            fontSize: '0.8rem',
                            boxShadow: vista === v ? '0 0 15px rgba(200,155,60,0.1)' : 'none',
                            transition: 'all 0.2s'
                        }}>
                        {v === 'classifica' ? '🏆 Classifica' : '📅 Calendario'}
                    </button>
                ))}
            </div>

            {/* Contenuto */}
            {vista === 'classifica'
                ? <Classifica torneoId={torneoSelezionato} />
                : <Calendario torneoId={torneoSelezionato} />
            }
        </div>
    );
}

const root = ReactDOM.createRoot(document.getElementById('react-root'));
root.render(<App />);