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

    if (loading) return <p style={{ color: 'var(--color-text)' }}>Caricamento classifica...</p>;

    return (
        <div>
            <h2>🏆 Classifica</h2>
            <table style={{ width: '100%', borderCollapse: 'collapse' }}>
                <thead>
                    <tr style={{ borderBottom: '1px solid var(--color-gold)', color: 'var(--color-gold)' }}>
                        <th style={{ padding: '0.5rem', textAlign: 'left' }}>#</th>
                        <th style={{ padding: '0.5rem', textAlign: 'left' }}>Squadra</th>
                        <th style={{ padding: '0.5rem', textAlign: 'center' }}>P</th>
                        <th style={{ padding: '0.5rem', textAlign: 'center' }}>V</th>
                        <th style={{ padding: '0.5rem', textAlign: 'center' }}>N</th>
                        <th style={{ padding: '0.5rem', textAlign: 'center' }}>S</th>
                        <th style={{ padding: '0.5rem', textAlign: 'center' }}>GF</th>
                        <th style={{ padding: '0.5rem', textAlign: 'center' }}>GS</th>
                        <th style={{ padding: '0.5rem', textAlign: 'center' }}>DR</th>
                        <th style={{ padding: '0.5rem', textAlign: 'center' }}>Pt</th>
                    </tr>
                </thead>
                <tbody>
                    {classifica.map((s, i) => (
                        <tr key={s.id} style={{
                            borderBottom: '1px solid var(--border-color)',
                            background: i === 0 ? 'rgba(200,155,60,0.1)' : 'transparent'
                        }}>
                            <td style={{ padding: '0.6rem', color: 'var(--color-text)' }}>{i + 1}</td>
                            <td style={{ padding: '0.6rem', color: 'var(--color-text-light)', fontWeight: i < 3 ? 'bold' : 'normal' }}>{s.nome}</td>
                            <td style={{ padding: '0.6rem', textAlign: 'center', color: 'var(--color-text)' }}>{s.vinte + s.pareggiate + s.perse}</td>
                            <td style={{ padding: '0.6rem', textAlign: 'center', color: 'var(--ionia)' }}>{s.vinte}</td>
                            <td style={{ padding: '0.6rem', textAlign: 'center', color: 'var(--color-text)' }}>{s.pareggiate}</td>
                            <td style={{ padding: '0.6rem', textAlign: 'center', color: 'var(--noxus)' }}>{s.perse}</td>
                            <td style={{ padding: '0.6rem', textAlign: 'center', color: 'var(--color-text)' }}>{s.golfatti}</td>
                            <td style={{ padding: '0.6rem', textAlign: 'center', color: 'var(--color-text)' }}>{s.golsubiti}</td>
                            <td style={{ padding: '0.6rem', textAlign: 'center', color: 'var(--color-text)' }}>{s.differenzaReti > 0 ? '+' + s.differenzaReti : s.differenzaReti}</td>
                            <td style={{ padding: '0.6rem', textAlign: 'center', color: 'var(--color-gold)', fontWeight: 'bold' }}>{s.punti}</td>
                        </tr>
                    ))}
                </tbody>
            </table>
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
        border: '1px solid var(--color-gold)',
        background: attivo ? 'var(--color-gold)' : 'transparent',
        color: attivo ? 'var(--bg-primary)' : 'var(--color-gold)',
        cursor: 'pointer', borderRadius: '2px', fontSize: '0.8rem'
    });

    const selectStyle = {
        background: 'var(--bg-secondary)',
        border: '1px solid var(--color-gold)',
        color: 'var(--color-gold)',
        padding: '0.4rem 0.8rem',
        borderRadius: '2px',
        fontSize: '0.85rem',
        cursor: 'pointer',
        minWidth: '200px'
    };

    if (loading) return <p style={{ color: 'var(--color-text)' }}>Caricamento calendario...</p>;

    return (
        <div>
            <h2>📅 Calendario</h2>

            {/* Filtri */}
            <div style={{
                display: 'flex', gap: '1rem', flexWrap: 'wrap',
                alignItems: 'center', marginBottom: '1.2rem'
            }}>
                {/* Filtro stato */}
                <div style={{ display: 'flex', gap: '0.5rem', flexWrap: 'wrap' }}>
                    {['TUTTE', 'COMPLETATA', 'IN_ATTESA'].map(f => (
                        <button key={f} onClick={() => setFiltroStato(f)} style={btnStyle(filtroStato === f)}>
                            {f === 'TUTTE' ? 'Tutte' : f === 'COMPLETATA' ? 'Completate' : 'In Attesa'}
                        </button>
                    ))}
                </div>

                {/* Filtro squadra — tendina */}
                <select
                    value={filtroSquadra}
                    onChange={e => setFiltroSquadra(e.target.value)}
                    style={selectStyle}
                >
                    <option value="TUTTE">⚔ Tutte le squadre</option>
                    {squadre.map(s => (
                        <option key={s} value={s}>{s}</option>
                    ))}
                </select>
            </div>

            {/* Partite */}
            {partiteFiltrate.map(p => {
                const risultato = getRisultato(p);
                return (
                    <div key={p.id} style={{
                        background: 'var(--bg-card)',
                        border: '1px solid var(--border-color)',
                        borderRadius: '4px',
                        padding: '0.8rem 1rem',
                        marginBottom: '0.5rem',
                        display: 'flex',
                        justifyContent: 'space-between',
                        alignItems: 'center',
                        gap: '0.5rem'
                    }}>
                        {/* Badge V/N/S */}
                        {risultato && (
                            <div style={{
                                fontWeight: 'bold', fontSize: '0.9rem',
                                color: risultato.colore,
                                minWidth: '20px', textAlign: 'center'
                            }}>
                                {risultato.testo}
                            </div>
                        )}

                        {/* Squadra casa */}
                        <div style={{
                            flex: 1, textAlign: 'right',
                            color: filtroSquadra === p.squadraCasa
                                ? 'var(--color-gold)' : 'var(--color-text-light)',
                            fontWeight: filtroSquadra === p.squadraCasa ? 'bold' : 'normal',
                            fontSize: 'clamp(0.75rem, 2vw, 0.95rem)'
                        }}>
                            {p.squadraCasa}
                        </div>

                        {/* Centro */}
                        <div style={{ padding: '0 0.8rem', textAlign: 'center', minWidth: '100px' }}>
                            {p.stato === 'COMPLETATA'
                                ? <span style={{ color: 'var(--color-gold)', fontWeight: 'bold', fontSize: '1.1rem' }}>
                                    {p.goalHome} — {p.goalGuest}
                                </span>
                                : <span style={{ color: 'var(--color-text)', fontSize: '0.8rem' }}>
                                    {formatData(p.dataOra)}
                                </span>
                            }
                            <div style={{ fontSize: '0.7rem', color: 'var(--color-text)', marginTop: '0.2rem' }}>
                                {p.luogo}
                            </div>
                        </div>

                        {/* Squadra ospite */}
                        <div style={{
                            flex: 1, textAlign: 'left',
                            color: filtroSquadra === p.squadraOspite
                                ? 'var(--color-gold)' : 'var(--color-text-light)',
                            fontWeight: filtroSquadra === p.squadraOspite ? 'bold' : 'normal',
                            fontSize: 'clamp(0.75rem, 2vw, 0.95rem)'
                        }}>
                            {p.squadraOspite}
                        </div>
                    </div>
                );
            })}

            {partiteFiltrate.length === 0 &&
                <p style={{ color: 'var(--color-text)' }}>Nessuna partita trovata.</p>
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
            <div style={{ marginBottom: '1.5rem', display: 'flex', gap: '1rem', alignItems: 'center', flexWrap: 'wrap' }}>
                <span style={{ color: 'var(--color-gold-light)' }}>Torneo:</span>
                {tornei.map(t => (
                    <button key={t.id} onClick={() => setTorneoSelezionato(t.id)}
                        style={{
                            padding: '0.4rem 1rem',
                            border: '1px solid var(--color-gold)',
                            background: torneoSelezionato === t.id ? 'var(--color-gold)' : 'transparent',
                            color: torneoSelezionato === t.id ? 'var(--bg-primary)' : 'var(--color-gold)',
                            cursor: 'pointer', borderRadius: '2px'
                        }}>
                        {t.nome}
                    </button>
                ))}
            </div>

            {/* Selettore vista */}
            <div style={{ marginBottom: '1.5rem', display: 'flex', gap: '1rem' }}>
                {['classifica', 'calendario'].map(v => (
                    <button key={v} onClick={() => setVista(v)}
                        style={{
                            padding: '0.5rem 1.5rem',
                            border: '1px solid var(--border-color)',
                            background: vista === v ? 'var(--bg-card)' : 'transparent',
                            color: vista === v ? 'var(--color-gold)' : 'var(--color-text)',
                            cursor: 'pointer', borderRadius: '2px',
                            textTransform: 'uppercase', letterSpacing: '1px', fontSize: '0.85rem'
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