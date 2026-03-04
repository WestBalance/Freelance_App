import { useMemo, useState } from 'react'

export default function SkillSelector({ options, selected, onChange }) {
  const [query, setQuery] = useState('')
  const [open, setOpen] = useState(false)

  const filtered = useMemo(() => {
    const q = query.toLowerCase()
    return options.filter(skill => skill.name.toLowerCase().includes(q))
  }, [options, query])

  const toggle = skill => {
    const has = selected.some(item => item.id === skill.id)
    if (has) {
      onChange(selected.filter(item => item.id !== skill.id))
      return
    }
    onChange([...selected, skill])
  }

  return (
    <div className="skill-selector">
      <input
        type="text"
        placeholder="Search skills..."
        value={query}
        onFocus={() => setOpen(true)}
        onBlur={() => setTimeout(() => setOpen(false), 100)}
        onChange={e => setQuery(e.target.value)}
      />
      {open && (
        <div className="skill-options">
          {filtered.map(skill => {
            const active = selected.some(item => item.id === skill.id)
            return (
              <button
                type="button"
                key={skill.id}
                className={active ? 'skill-option active' : 'skill-option'}
                onMouseDown={e => e.preventDefault()}
                onClick={() => toggle(skill)}
              >
                {skill.name}
              </button>
            )
          })}
          {!filtered.length && <span className="skill-empty">No matches</span>}
        </div>
      )}
      <div className="skill-tags">
        {selected.map(skill => (
          <button type="button" key={skill.id} className="tag" onClick={() => toggle(skill)}>{skill.name} ×</button>
        ))}
      </div>
    </div>
  )
}
